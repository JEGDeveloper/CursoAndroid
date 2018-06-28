package test.yespinoza.androidproject.Model.Utils;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import test.yespinoza.androidproject.Model.CardView;

public class  SwipeListener {

    public RecyclerView.Adapter adapter;
    public List<CardView> items;

    public SwipeListener(RecyclerView.Adapter adapter, List<CardView> items){
        this.adapter = adapter;
        this.items = items;
    }

    public boolean canSwipeLeft(int position) {
        return true;
    }
    public boolean canSwipeRight(int position) {
        return true;
    }

    public boolean canSwipe(int position) {
        return true;
    }

    public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            items.remove(position);
            adapter.notifyItemRemoved(position);
        }
        adapter.notifyDataSetChanged();
    }

    public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            items.remove(position);
            adapter.notifyItemRemoved(position);
        }
        adapter.notifyDataSetChanged();
    }
}
