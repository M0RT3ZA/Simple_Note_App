package ir.morteza_aghighi.simplenoteapp.view

import android.app.Application
import android.graphics.*
import android.util.DisplayMetrics
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ir.morteza_aghighi.simplenoteapp.R
import kotlinx.coroutines.*
import kotlin.math.abs
import kotlin.math.roundToInt

class SwipeUtil(private val application: Application, private val adapter: NotesRecyclerAdapter) :
    ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    private val tAG = "SwipeUtil"

    /*Handling Coroutine Errors*/
    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(tAG, "$tAG Exception Error: $throwable")
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        viewHolder.setIsRecyclable(false)
        val removePendingJob = CoroutineScope(Dispatchers.Main + errorHandler)
        val snackBar = Snackbar.make(
            viewHolder.itemView, "If You Want to Cancel Press Undo",
            Snackbar.LENGTH_INDEFINITE
        )
        snackBar.setAction("Undo") {
            if (removePendingJob.isActive){
                removePendingJob.cancel()
                viewHolder.setIsRecyclable(true)
                adapter.notifyItemChanged(viewHolder.adapterPosition)
                snackBar.dismiss()
            }
        }
        snackBar.setActionTextColor(Color.BLUE)
        snackBar.show()
        removePendingJob.launch {
            delay(5000)
            if (isActive)
                snackBar.dismiss()
            viewHolder.setIsRecyclable(true)
            adapter.remove(viewHolder.adapterPosition,application)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        // Get RecyclerView item from the ViewHolder
        val itemView = viewHolder.itemView
        val isCancelled = dX == 0f && !isCurrentlyActive
        if (isCancelled) {
            adapter.notifyItemChanged(viewHolder.adapterPosition)
            viewHolder.itemView.alpha = 1.0f


            return
        }
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            // Fade out the view as it is swiped out of the parent's bounds
            val alpha = ALPHA_FULL - abs(dX) / viewHolder.itemView.width.toFloat()
            viewHolder.itemView.alpha = alpha
            viewHolder.itemView.translationX = dX
            val backgroundCornerOffset = 0

/*             dX (float) is the amount of horizontal displacement caused by user's action.
             If the horizontal displacement is positive the item is being
             swiped to the RIGHT, if it is negative the item is being
             swiped to the LEFT.*/
            val bg = if (dX < 0) RectF(
                (itemView.right + dX.toInt() - backgroundCornerOffset).toFloat(),
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            else RectF(
                (-itemView.left + dX.toInt() + backgroundCornerOffset).toFloat(),
                itemView.top.toFloat(),
                -itemView.left.toFloat(),
                itemView.bottom.toFloat()
            )
            val backgroundColor = Paint()
            backgroundColor.color = Color.parseColor("#FF0000")
            c.drawRoundRect(bg,
                application.applicationContext.resources.getDimension(R.dimen.round_corner_radius),
                application.applicationContext.resources.getDimension(R.dimen.round_corner_radius),
                backgroundColor)
            drawText("Deleting...", c, bg, backgroundColor, dX)
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    private fun drawText(text: String, c: Canvas, button: RectF, p: Paint, dX: Float) {
        val textSize = 100f
        p.color = Color.WHITE
        p.isAntiAlias = true
        p.textSize = textSize
        val textWidth = p.measureText(text)
        p.color = application.applicationContext.getColor(R.color.white)
        c.drawText(text, button.centerX() - textWidth / 2, button.centerY() + textSize / 2, p)
/*        if (dX < 0) c.drawText(
            text,
            button.left + convertDpToPx(16),
            button.centerY() + textSize / 2,
            p
        )
        else c.drawText(text, button.right + convertDpToPx(16), button.centerY() + textSize / 2, p)*/
    }

    private fun convertDpToPx(dp: Int): Float {
        return (dp * (application.applicationContext.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }
    private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        val mClearPaint = Paint()
        mClearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        c.drawRect(left, top, right, bottom, mClearPaint)
    }
    companion object {
        const val ALPHA_FULL = 1.0f
    }
}
