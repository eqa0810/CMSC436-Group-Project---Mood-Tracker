package com.example.group_project
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
class LogMoodFragment : Fragment(R.layout.fragment_log_mood) {

    private var adView: AdView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adContainer = view.findViewById<LinearLayout>(R.id.ad_container_log)

        val banner = AdView(requireContext())
        banner.setAdSize(AdSize.BANNER)
        banner.adUnitId = "ca-app-pub-3940256099942544/6300978111"  // test id

        adContainer.addView(banner)

        val request = AdRequest.Builder().build()
        banner.loadAd(request)

        adView = banner
    }

    override fun onPause() {
        adView?.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        adView?.resume()
    }

    override fun onDestroyView() {
        adView?.destroy()
        super.onDestroyView()
    }
}
