package com.intertech.radioplayer.ui.main

import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.intertech.radioplayer.R
import com.intertech.radioplayer.data.model.RadioPlayList
import com.intertech.radioplayer.data.network.ApiService
import com.intertech.radioplayer.data.network.Resource
import com.intertech.radioplayer.data.network.handleApiError
import com.intertech.radioplayer.data.repository.PlayListRepository
import com.intertech.radioplayer.ui.base.BaseActivity
import com.intertech.radioplayer.util.PlayerConfig
import kotlinx.android.synthetic.main.activity_main.*


// 1. Create an app with 2 views; Home view and Recently Played View. 
class MainActivity : BaseActivity<PlayListViewModel, PlayListRepository>() {
    private lateinit var player: SimpleExoPlayer
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    //creating Object of InterstitialAd
    private var mInterstitialAd: InterstitialAd? = null
    private var isAdLoadedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Show InterstitialAd
        showAds()

        // Initialize bottom navigation menu to switch between home and recently played view
        setupBottomNavigationMenu()

        // Call play list api to get latest play list
        getRadioPlayList()

        // Initialize exo player
        initializePlayer()

        /**
         * Handle play/pause event
         */
        iv_play_stream.setOnClickListener {
            if (playWhenReady)
                startPlayer()
            else
                pausePlayer()
        }

    }

    /**
     * Initialize view model instance
     */
    override fun getViewModel() = PlayListViewModel::class.java

    /**
     * Initialize Repository instance
     */
    override fun getActivityRepository() = PlayListRepository(
        remoteDataSource.buildApi(
            ApiService::class.java
        )
    )

    //5. Add a persistent player bar to the app which plays a live radio stream ( provided below ). The player will be fixed above the bottom navigation bar and is visible on all views.
    private fun initializePlayer() {

        // Create a player instance.
        player = SimpleExoPlayer.Builder(this).build()
        playerView.player = player

        // Set steam url to player
        val mediaItem: MediaItem = MediaItem.fromUri(PlayerConfig.RADIO_STREAM_URL)
        player.setMediaItem(mediaItem)

        val dataSource: DataSource.Factory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, getString(R.string.app_name))
        )
        val extractorsFactory = DefaultExtractorsFactory().setConstantBitrateSeekingEnabled(true)

        val progressiveMediaSource =
            ProgressiveMediaSource.Factory(dataSource, extractorsFactory).createMediaSource(
                MediaItem.fromUri(
                    PlayerConfig.RADIO_STREAM_URL
                )
            )

        player.seekTo(currentWindow, playbackPosition)
        // Set the media source to be played.
        player.setMediaSource(progressiveMediaSource)
        // Prepare the player.
        player.prepare()
    }

    /**
     * Pause player
     */
    private fun pausePlayer() {
        player.playWhenReady = false
        player.playbackState
        playWhenReady = true
        iv_play_stream.setImageResource(R.drawable.exo_icon_play)
        iv_play_stream.setColorFilter(
            ContextCompat.getColor(this, R.color.colorPrimary),
            android.graphics.PorterDuff.Mode.MULTIPLY
        );
    }

    /**
     * Start player
     */
    private fun startPlayer() {
        player.playWhenReady = true
        player.playbackState
        playWhenReady = false
        iv_play_stream.setImageResource(R.drawable.exo_icon_pause)
        iv_play_stream.setColorFilter(
            ContextCompat.getColor(this, R.color.grayColor),
            android.graphics.PorterDuff.Mode.MULTIPLY
        );
    }

    /**
     * Stop player
     */
    private fun releasePlayer() {
        pausePlayer()
        player.stop()
        player.release()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        releasePlayer()
    }

    //4. Add a bottom navigation bar to allow a user to switch between the “Home” view and “Recently Played” view 
    private fun setupBottomNavigationMenu(){
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_recently_played
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    /**
     * Make a play list api call request
     */
    private fun getRadioPlayList(){
        viewModel.playList()

        // 6. Display the song title, artist name, and album art of the song currently playing on the player bar. The player bar should also have a play/stop toggle button for starting the radio stream.

        /**
         * observe api data response and perform action
         */
        viewModel.playListResponse.observe(this, Observer {

            when (it) {
                is Resource.Success -> {
                    /**
                     * Hide progressbar dialog
                     */
                    dismissProgressDialog()

                    /**
                     * Received data from server
                     */
                    val playListData = Gson().toJson(it.value)

                    /**
                     * Convert received data string into array list
                     */
                    val playedListArray = Gson().fromJson(
                        playListData,
                        Array<RadioPlayList.RadioPlayListItem>::class.java
                    )
                    val playedList = ArrayList(playedListArray.toMutableList())

                    /**
                     * Load data on screen only if list size is greater than zero
                     */
                    if (playedList.size > 0) {
                        /**
                         * Display currently playing song details
                         */
                        val playedListData = playedList[0]
                        tv_player_title.text = playedListData.name
                        tv_player_artist.text = playedListData.artist
                        Glide
                            .with(this)
                            .load(playedListData.image_url)
                            .centerCrop()
                            .placeholder(R.drawable.music_placeholder)
                            .into(iv_player_art)
                    }

                }
                is Resource.Failure -> {
                    /**
                     * Hide progressbar dialog
                     */
                    dismissProgressDialog()
                    /**
                     * Display error message
                     */
                    handleApiError(this, it)
                }
                is Resource.Loading -> {
                    /**
                     * Show Progressbar dialog
                     */
                    showProgressDialog()
                }
            }
        })
    }

    //9. Super Bonus - Display ads on the app. Before playing the stream, show interstitial ads. 
    private fun showAds(){
        //initializing the Google Admob SDK
        MobileAds.initialize(
            this
        ) { initializationStatus ->
            //The Google AdMob Sdk Initialization is Completed
            Log.d("AdMob==>", "AdMob Sdk Initialize $initializationStatus")
            loadInterstitialAd()
        }
    }

    private fun loadInterstitialAd() {
        // Creating  a Ad Request
        val adRequest: AdRequest = AdRequest.Builder().build()

        // creating Interstitial Ad load callback
        InterstitialAd.load(
            this,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // An ad is failed to load
                    Log.d("AdMob==>", adError.message)
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // An ad is loaded
                    Log.d("AdMob==>", "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                    if (!isAdLoadedOnce) {
                        showInterstitialAd()
                    }
                }
            })

        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d("AdMob==>", "Ad was dismissed.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d("AdMob==>", "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d("AdMob==>", "Ad showed fullscreen content.")
                mInterstitialAd = null
            }
        }
    }

    private fun showInterstitialAd() {
        if (mInterstitialAd != null && !player.isPlaying) {

            //showing the ad Interstitial Ad if it is loaded
            mInterstitialAd?.show(this@MainActivity)
            isAdLoadedOnce = true

            // An Interstitial ad is shown to the user
            Log.d("AdMob==>", "Interstitial Ad is loading")
        } else {
            //Load the Interstitial ad if it is not loaded
            loadInterstitialAd()

            // An ad is not loaded
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }
}