package com.ifanr.tangzhi.ui.base.model

import android.util.Log
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.recyclerview.widget.DiffUtil
import com.airbnb.epoxy.*
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.repository.baas.datasource.BaseDataSource
import com.ifanr.tangzhi.repository.baas.datasource.TransformerDataSource
import com.ifanr.tangzhi.util.uuid
import com.ifanr.tangzhi.workerHandler

/**
 * PagedList 里的 notifyExecutor 与 Controller 里的 modelBuildingHandler 是同一线程
 */

abstract class BaseEpoxyController: EpoxyController(
    workerHandler, workerHandler
)

abstract class BaseTypedController<T>: TypedEpoxyController<T>(
    workerHandler, workerHandler
)

abstract class BaseTyped2Controller<T, U>: Typed2EpoxyController<T, U>(
    workerHandler, workerHandler
)

abstract class BaseTyped3Controller<T, U, V>: Typed3EpoxyController<T, U, V>(
    workerHandler, workerHandler
)

abstract class BaseTyped4Controller<T, U, V, W>: Typed4EpoxyController<T, U, V, W>(
    workerHandler, workerHandler
)

abstract class BasePagedListController<T>: PagedListEpoxyController<T> {

    companion object {
        private const val TAG = "BasePagedListController"
    }

    private val footer by lazy {
        ListEndSlogonModel_().id(uuid())
    }

    private var dataSource: DataSource<*, *>? = null

    constructor(
        itemDiffCallback: DiffUtil.ItemCallback<T>
    ) : super(workerHandler, workerHandler, itemDiffCallback)

    constructor() : super(workerHandler, workerHandler)

    /**
     * use it instead of [submitList]
     */
    fun submitPagedList(newList: PagedList<T>) {
        dataSource = newList.dataSource
        submitList(newList)
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        super.addModels(models)
        val endOfList = (dataSource as? BaseDataSource)?.endOfList == true ||
                (dataSource as? TransformerDataSource<*, *>)?.endOfList == true
        if (models.isNotEmpty() && endOfList) {
            add(footer)
        }
    }
}

abstract class LoadMoreAwaredController<T: Collection<*>>: BaseTypedController<T>() {

    var loadMoreListener: () -> Unit = {}

    override fun onModelBound(
        holder: EpoxyViewHolder,
        boundModel: EpoxyModel<*>,
        position: Int,
        previouslyBoundModel: EpoxyModel<*>?
    ) {
        val count = currentData?.size ?: 0
        if (count > 0 && position + 1 + Const.PRE_FETCH_DISTANCE > count) {
            loadMoreListener.invoke()
        }
    }
}