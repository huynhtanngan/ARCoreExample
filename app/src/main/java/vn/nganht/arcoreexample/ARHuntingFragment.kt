package vn.nganht.arcoreexample

import android.content.Context
import android.view.MotionEvent
import com.google.ar.core.Anchor
import com.google.ar.core.exceptions.UnavailableException
import com.google.ar.sceneform.HitTestResult
import com.google.ar.sceneform.rendering.Renderable

class ArHuntingFragment : AbsARFragment() {
    override val imageMap: Map<String, Int>
        get() = mapOf(
            "yard.jpg" to R.raw.table,
            "desert.jpg" to R.raw.tree,
            "zalopay.png" to R.raw.pig
        )

    override fun onTapNode(hitTestResult: HitTestResult?, motionEvent: MotionEvent?) {
        sessionListener?.onTapNode(hitTestResult, motionEvent)
    }

    private var sessionListener: SessionListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        sessionListener = context as? SessionListener
    }

    override val assetsDBName: String
        get() = "imagelist.imgdb"

    override fun handleSessionException(sessionException: UnavailableException?) {
        super.handleSessionException(sessionException)
        sessionListener?.onSessionException(sessionException)
    }

    override fun addNodeToScene(anchor: Anchor, renderable: Renderable) {
        super.addNodeToScene(anchor, renderable)
        sessionListener?.onAddNodeToScene(anchor, renderable)
    }

    interface SessionListener {
        fun onSessionException(sessionException: UnavailableException?)

        fun onTapNode(hitTestResult: HitTestResult?, motionEvent: MotionEvent?)

        fun onAddNodeToScene(anchor: Anchor, renderable: Renderable)
    }
}