package com.xiaomai.geek.ui.module.effects;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaomai.geek.R;
import com.xiaomai.geek.data.module.Effect;
import com.xiaomai.geek.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiaoMai on 2017/11/21.
 */

public class EffectListActivity extends BaseActivity {

    private MyAdapter mAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_effect_list;
    }

    @Override
    public void initViews() {
        super.initViews();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new MyAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Effect effect) {
                final String clazzName = effect.getClazzName();
                if (TextUtils.isEmpty(clazzName)) {
                    EffectDetailActivity.launch(mContext, effect);
                } else {
                    try {
                        startActivity(new Intent(mContext, Class.forName(clazzName)));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void loadData() {
        super.loadData();
        final List<Effect> effects = new ArrayList<>();
        effects.add(new Effect(getString(R.string.label_layout), R.layout.label_layout));
        effects.add(new Effect(getString(R.string.flow_layout), FlowLayoutActivity.class.getName()));
        mAdapter.setList(effects);
    }

    private static class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Nullable
        private List<Effect> list;
        @Nullable
        private OnItemClickListener onItemClickListener;

        public void setList(@Nullable List<Effect> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
            onItemClickListener = listener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_effect, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            final Effect effect = getItem(position);
            if (effect == null) {
                return;
            }
            Holder holder;
            if (viewHolder instanceof Holder) {
                holder = (Holder) viewHolder;
                holder.textView.setText(effect.getTitle());
                if (onItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClickListener.onItemClick(effect);
                        }
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        public Effect getItem(int position) {
            if (list == null) {
                return null;
            }
            return list.get(position);
        }
    }

    private static class Holder extends RecyclerView.ViewHolder {
        TextView textView;

        private Holder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.tv_title);
        }
    }

    private interface OnItemClickListener {
        void onItemClick(Effect effect);
    }
}
