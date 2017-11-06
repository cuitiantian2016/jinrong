package com.honglu.future.ui.trade.bean;

/**
 * Created by zq on 2017/11/6.
 */

public class HoldDetailBean {
        private int count;
        private String createPrice;
        private String openDate;
        private String openTime;
        private String sxf;
        private int type;
        private String useMargin;
        public void setCount(int count) {
            this.count = count;
        }
        public int getCount() {
            return count;
        }

        public void setCreatePrice(String createPrice) {
            this.createPrice = createPrice;
        }
        public String getCreatePrice() {
            return createPrice;
        }

        public void setOpenDate(String openDate) {
            this.openDate = openDate;
        }
        public String getOpenDate() {
            return openDate;
        }

        public void setOpenTime(String openTime) {
            this.openTime = openTime;
        }
        public String getOpenTime() {
            return openTime;
        }

        public void setSxf(String sxf) {
            this.sxf = sxf;
        }
        public String getSxf() {
            return sxf;
        }

        public void setType(int type) {
            this.type = type;
        }
        public int getType() {
            return type;
        }

        public void setUseMargin(String useMargin) {
            this.useMargin = useMargin;
        }
        public String getUseMargin() {
            return useMargin;
        }

}
