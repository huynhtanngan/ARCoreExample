package vn.nganht.arcoreexample

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.HitTestResult
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment

abstract class AbsARFragment : ArFragment() {
    protected abstract val assetsDBName: String

    protected abstract val imageMap: Map<String, Int>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        planeDiscoveryController.hide()
        planeDiscoveryController.setInstructionView(null)
    }

    override fun getSessionConfiguration(session: Session?): Config {
        val config = Config(session)
        val inputStream = requireContext().assets.open(assetsDBName)
        val augmentedImageDatabase = AugmentedImageDatabase.deserialize(session, inputStream)
        config.augmentedImageDatabase = augmentedImageDatabase
        return config
    }

    override fun onUpdate(frameTime: FrameTime?) {
        super.onUpdate(frameTime)
        val frame = arSceneView.arFrame
        val augmentedImages = frame.getUpdatedTrackables(AugmentedImage::class.java)
        augmentedImages.forEach {
            if (it.trackingState == TrackingState.TRACKING && imageMap.containsKey(it.name)) {
                renderObject(it.createAnchor(it.centerPose), imageMap.get(it.name))
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun renderObject(anchor: Anchor, model: Int?) {
        ModelRenderable.builder()
            .setSource(requireContext(), model ?: return)
            .build()
            .thenAccept { renderable -> addNodeToScene(anchor, renderable) }
            .exceptionally {
                null
            }

    }

    protected open fun addNodeToScene(anchor: Anchor, renderable: Renderable) {
        val anchorNode = AnchorNode(anchor)
        anchorNode.renderable = renderable
        anchorNode.setOnTapListener { hitTestResult, motionEvent ->
            onTapNode(hitTestResult, motionEvent)
        }
        arSceneView.scene.addChild(anchorNode)
    }

    abstract fun onTapNode(hitTestResult: HitTestResult?, motionEvent: MotionEvent?)
}