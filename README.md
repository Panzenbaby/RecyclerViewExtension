# RecyclerViewExtension

Extends the RecyclerView with some features.

[ ![Download](https://api.bintray.com/packages/panzenbaby/RecyclerViewExtensions/RecyclerViewExtensions/images/download.svg) ](https://bintray.com/panzenbaby/RecyclerViewExtensions/RecyclerViewExtensions/_latestVersion)

The plan is to fill up this library with some useful extensions over time.

# Installation

RecycleViewExtension is distributed via jCenter. [ ![Download](https://api.bintray.com/packages/panzenbaby/RecyclerViewExtensions/RecyclerViewExtensions/images/download.svg) ](https://bintray.com/panzenbaby/RecyclerViewExtensions/RecyclerViewExtensions/_latestVersion)

```groovy
dependencies {
    implementation 'de.panzenbaby:RecyclerViewExtensions:1.0'
}
```

# Header/Footer View

## Motivation

In the first version this library provides the possibility to add a header or a footer view to a RecyclerView.
This functionality is known from the old ListView where you could simple add a view as header or footer.
Unfortunately Google did not implement this feature for the RecyclerView.

On StackOverflow you can find some posts where users suggests to use special view types for header or footer views
inside of the RecyclerView.Adapter. If you do so the adapter of your recycler view creates views which may be never be
recycled because there is no other position which use the same layout as you header or footer.

Often you set a collection of objects to the recycler view to represent the data which should be displayed.
In case you want to use a header or footer view you have to create some special objects which represents them. If you plan to
manipulate your collection e.g. you want to change the order you have to add special logic for the
created header or footer data.

## Idea

Use views as header or footer which does not be children of the recycler view.

## HowTo

##### 1. Create your layout
Place your RecyclerView as well as your header and/or footer view in the layout of your activity, fragment or view.
You can use any view
you want as header or footer.

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    >

    <View
        android:id="@+id/recyclerHeader"
        android:layout_width="match_parent"
        android:layout_height="@dimen/header_height"
        android:background="@color/colorPrimary"
        />

    <View
        android:id="@+id/recyclerFooter"
        android:layout_width="match_parent"
        android:layout_height="@dimen/footer_height"
        android:background="@color/colorAccent"
        />

    <android.support.v4.widget.SwipeRefreshLayout
        android:id="@+id/swipeRefresh"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <android.support.v7.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            />
    </android.support.v4.widget.SwipeRefreshLayout>
</FrameLayout>
```

##### 2. Initialize the RecyclerView

Initialize the RecyclerView as usual. Set at least the layout manager and the adapter.

```java
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new SimpleRecyclerAdapter(20));
```

##### 3. Attach the header or footer view

If you want to attach a view as header use the RecyclerHeaderHelper and if you want to attach it as a footer use the
RecyclerFooterHelper.

```java
        View recyclerHeader = findViewById(R.id.recyclerHeader);
        View recyclerFooter = findViewById(R.id.recyclerFooter);
        mHeaderHelper.attach(recyclerView, recyclerHeader);
        mFooterHelper.attach(recyclerView, recyclerFooter);
```

##### 4. Insert data

If you use a header view and insert data before the first item of your adapter you have to call refreshItemDecoration.
The same goes for the footer view if you want to insert data after the last item.

```java
        recyclerView.getAdapter().notifyItemInserted(0);
        mHeaderHelper.refreshItemDecoration(recyclerView);
```

or

```java
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        adapter.notifyItemInserted(adapter.getItemCount());
        mFooterHelper.refreshItemDecoration(recyclerView);
```

##### 5. Done