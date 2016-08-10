package kistudio.com.recyclerviewdrag;

/**
 * Created by Android on 10.08.2016.
 */
public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
