package com.tavarus.artabletop.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

inline fun <reified T : View> LayoutInflater.inflateAs(
    @LayoutRes resource: Int,
    root: ViewGroup?,
    attachToRoot: Boolean
): T = inflate(resource, root, attachToRoot) as T

inline fun <reified T : View> ViewGroup.inflateAs(@LayoutRes resource: Int, attachToRoot: Boolean = false): T =
    LayoutInflater.from(context).inflateAs(resource, this, attachToRoot)
