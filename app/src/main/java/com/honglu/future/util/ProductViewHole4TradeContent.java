package com.honglu.future.util;

import android.content.Context;
import android.widget.TextView;

import com.xulu.mpush.message.RequestMarketMessage;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zq on 2017/11/30.
 * 交易大厅局部刷新所用
 */

public class ProductViewHole4TradeContent {
    public class ProductView {
        public TextView tv_buyup_rate;
        public TextView tv_buydown_rate;
        public TextView tv_vol;
        public String tag;
    }

    public HashMap<String, ProductView> listProductView;
    Context context;
    String codes;
    List<RequestMarketMessage> optionalList;

    public ProductViewHole4TradeContent(Context context, String codes) {
        this.context = context;
        this.codes = codes;
        listProductView = new HashMap<String, ProductView>();
        initProductViewList();
    }

    public void setOptionalList(List<RequestMarketMessage> optionalList) {
        this.optionalList = optionalList;
    }

    /**
     * 控件初始化
     */
    void initProductViewList() {
        String[] allProducts = codes.split(",");
        for (int i = 0; i < allProducts.length; i++) {
            ProductView productView = new ProductView();
            productView.tag = allProducts[i];
            listProductView.put(allProducts[i], productView);
        }
    }

    /**
     * 刷新界面
     *
     * @param optional
     */
    public void updateProductViewListDisplay(RequestMarketMessage optional) {
        if (optional == null)
            return;
        if (listProductView == null)
            return;
        String code = optional.getInstrumentID();
        ProductView productView = listProductView.get(code);
        if (productView == null)
            return;
        if (productView.tv_buyup_rate != null) {
            productView.tv_buyup_rate.setText(optional.getAskPrice1());
        }
        if (productView.tv_buydown_rate != null) {
            productView.tv_buydown_rate.setText(optional.getBidPrice1());
        }
        if (productView.tv_vol != null) {
            productView.tv_vol.setText(optional.getVolume());
        }
    }
}
