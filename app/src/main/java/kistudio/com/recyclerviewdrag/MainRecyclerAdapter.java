package kistudio.com.recyclerviewdrag;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Android on 10.08.2016.
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter implements ItemTouchHelperAdapter {

    Context context;
    ArrayList<RecyclerObject> items;

    public MainRecyclerAdapter(Context context, ArrayList<RecyclerObject> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_contact_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerObject ro = items.get(position);
        ((MyViewHolder) holder).tvName.setText(ro.getName());
        ((MyViewHolder) holder).tvPhone.setText(ro.getPhone());
        ((MyViewHolder) holder).itemView.setOnClickListener(new RoOnClickListener(ro, holder.itemView.getContext()) {});
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onItemDismiss(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(items, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(items, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView tvPhone;
        public TextView tvName;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);
            tvName = (TextView) itemView.findViewById(R.id.tvNameContactItem);
            tvPhone = (TextView) itemView.findViewById(R.id.tvPhoneContactItem);
        }
    }

    private class RoOnClickListener implements View.OnClickListener {

        private final RecyclerObject ro;
        private final Context context;

        public RoOnClickListener(RecyclerObject ro, Context c) {
            this.ro = ro;
            this.context = c;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + ro.getPhone()));
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            context.startActivity(intent);
        }
    }
}
