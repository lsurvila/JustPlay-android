package com.justplay.android.view;

import rx.Observable;

public interface RxLifecycleView<T> {
    Observable<T> getLifecycle();
}
