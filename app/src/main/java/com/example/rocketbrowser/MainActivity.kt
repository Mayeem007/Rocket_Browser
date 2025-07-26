import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {

    private lateinit var loadingContainer: FrameLayout
    private lateinit var loadingIcon: ImageView

    private lateinit var pulseScaleXAnimator: ObjectAnimator
    private lateinit var pulseScaleYAnimator: ObjectAnimator
    private lateinit var pulseColorAnimator: ObjectAnimator // Optional, for color changes

    private lateinit var refreshRotateAnimator: ObjectAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadingContainer = findViewById(R.id.loading_container)
        loadingIcon = findViewById(R.id.loading_icon)

        setupPulseAnimation()
        setupRefreshAnimation()

        // Setup WebView & URL entry etc...
        // Listen to WebView loading state:
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress < 100) {
                    showLoadingIndicator()
                } else {
                    showRefreshIndicator()
                }
            }
        }
    }

    private fun setupPulseAnimation() {
        pulseScaleXAnimator = ObjectAnimator.ofFloat(loadingIcon, View.SCALE_X, 1f, 1.2f)
        pulseScaleXAnimator.duration = 600
        pulseScaleXAnimator.repeatMode = ObjectAnimator.REVERSE
        pulseScaleXAnimator.repeatCount = ObjectAnimator.INFINITE

        pulseScaleYAnimator = ObjectAnimator.ofFloat(loadingIcon, View.SCALE_Y, 1f, 1.2f)
        pulseScaleYAnimator.duration = 600
        pulseScaleYAnimator.repeatMode = ObjectAnimator.REVERSE
        pulseScaleYAnimator.repeatCount = ObjectAnimator.INFINITE

        // Optional: Color animator requires complex setup, can use ValueAnimator to update drawable tint.

        val pulseSet = AnimatorSet()
        pulseSet.playTogether(pulseScaleXAnimator, pulseScaleYAnimator)
        pulseSet.start()
    }

    private fun setupRefreshAnimation() {
        refreshRotateAnimator = ObjectAnimator.ofFloat(loadingIcon, View.ROTATION, 0f, 360f)
        refreshRotateAnimator.duration = 1000
        refreshRotateAnimator.repeatCount = ObjectAnimator.INFINITE
    }

    private fun showLoadingIndicator() {
        runOnUiThread {
            if (loadingContainer.visibility != View.VISIBLE) {
                loadingContainer.visibility = View.VISIBLE
                loadingIcon.setImageResource(R.drawable.ic_capsule)

                // Start pulsing scale animation
                pulseScaleXAnimator.start()
                pulseScaleYAnimator.start()
                refreshRotateAnimator.cancel()
                loadingIcon.rotation = 0f
            }
        }
    }

    private fun showRefreshIndicator() {
        runOnUiThread {
            loadingIcon.setImageResource(R.drawable.ic_refresh)

            // Stop pulse animation
            pulseScaleXAnimator.cancel()
            pulseScaleYAnimator.cancel()

            // Start refresh rotation animation
            refreshRotateAnimator.start()
        }
    }

    fun hideLoadingIndicator() {
        runOnUiThread {
            pulseScaleXAnimator.cancel()
            pulseScaleYAnimator.cancel()
            refreshRotateAnimator.cancel()

            loadingContainer.visibility = View.GONE
            loadingIcon.rotation = 0f
        }
    }
}
