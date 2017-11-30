package com.honglu.future.ui.trade.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.ui.trade.bean.OpenTransactionListBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.util.ProductViewHole4TradeContent;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zq on 2017/10/26.
 */

public class OpenTransactionAdapter extends ArrayAdapter<ProductListBean> {
    List<ProductListBean> objects;
    Context context;
    ProductViewHole4TradeContent productViewHole4TradeContent;

    public interface OnRiseDownClickListener {
        void onRiseClick(View view, ProductListBean bean);

        void onDownClick(View view, ProductListBean bean);

        void onItemClick(ProductListBean bean);
    }

    private OnRiseDownClickListener mListener;

    public void setOnRiseDownClickListener(OnRiseDownClickListener listener) {
        mListener = listener;
    }

    public OpenTransactionAdapter(Context context, int resource, List<ProductListBean> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    public void setProductViewHole4TradeContent(ProductViewHole4TradeContent productViewHole4TradeContent) {
        this.productViewHole4TradeContent = productViewHole4TradeContent;
    }

    public ProductViewHole4TradeContent getProductViewHole4TradeContent() {
        return productViewHole4TradeContent;
    }

    public void setItems(List<ProductListBean> list) {
        try {
            if (list == null || list.size() == 0)
                return;

        } catch (Exception e) {
            e.printStackTrace();
        }
        objects.clear();

        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                objects.add(list.get(i));
            }

            notifyDataSetChanged();
        }

    }

    public int getProductIndex(String codes) {
        for (ProductListBean productObj : objects) {
            if (codes.equals(productObj.getInstrumentId())) {
                return objects.indexOf(productObj);
            }
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_open_transaction, null);
        }
        TextView mTvProductName = (TextView) convertView.findViewById(R.id.tv_product_name);
        TextView mTvNum= (TextView) convertView.findViewById(R.id.tv_num);
        TextView mTvRise= (TextView) convertView.findViewById(R.id.tv_rise);
        TextView mTvDown= (TextView) convertView.findViewById(R.id.tv_down);
        TextView mTvClosed= (TextView) convertView.findViewById(R.id.tv_closed);
        LinearLayout mLlRise= (LinearLayout) convertView.findViewById(R.id.ll_rise);
        LinearLayout mLlDown= (LinearLayout) convertView.findViewById(R.id.ll_down);
        LinearLayout goKline = (LinearLayout) convertView.findViewById(R.id.go_kline);
        View mColorLine= convertView.findViewById(R.id.v_colorLine);

        final ProductListBean item = objects.get(position);

        if (productViewHole4TradeContent != null) {
            ProductViewHole4TradeContent.ProductView productView = productViewHole4TradeContent.listProductView.get(item.getInstrumentId());
            if (productView != null) {
                productView.tv_buyup_rate = mTvRise;
                productView.tv_buydown_rate = mTvDown;
                productView.tv_vol = mTvNum;
                productView.tag = item.getExcode() + "|" + item.getInstrumentId();
            }
        }

        mTvProductName.setText(item.getInstrumentName());
        mTvNum.setText(item.getTradeVolume());
        mTvRise.setText(item.getAskPrice1());
        mTvDown.setText(item.getBidPrice1());
//        mTvRiseRadio.setText(item.getLongRate() + "%");
//        mTvDownRadio.setText((100 - Integer.valueOf(item.getLongRate())) + "%");
        if (item.getIsClosed().equals("2")) {
            mTvClosed.setVisibility(View.VISIBLE);
        } else {
            mTvClosed.setVisibility(View.GONE);
        }
        mLlRise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRiseClick(v, item);
            }
        });

        mLlDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDownClick(v, item);
            }
        });

        goKline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(item);
            }
        });
        if (position == objects.size() -1){
            mColorLine.setVisibility(View.INVISIBLE);
        }else {
            mColorLine.setVisibility(View.VISIBLE);
        }

        return convertView;
    }


}
