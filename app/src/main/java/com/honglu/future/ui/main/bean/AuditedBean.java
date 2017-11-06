package com.honglu.future.ui.main.bean;

/**
 * Created by zhuaibing on 2017/11/4
 */

public class AuditedBean {

    /**
     * resultModel : {"success":true,"data":{"isAudit":"1"}}
     * appAuditQueryForm : {"appType":0,"marketCode":"official","versionNumber":"1.0.0"}
     */

    private ResultModelBean resultModel;
    private AppAuditQueryFormBean appAuditQueryForm;

    public ResultModelBean getResultModel() {
        return resultModel;
    }

    public void setResultModel(ResultModelBean resultModel) {
        this.resultModel = resultModel;
    }

    public AppAuditQueryFormBean getAppAuditQueryForm() {
        return appAuditQueryForm;
    }

    public void setAppAuditQueryForm(AppAuditQueryFormBean appAuditQueryForm) {
        this.appAuditQueryForm = appAuditQueryForm;
    }

    public static class ResultModelBean {
        /**
         * success : true
         * data : {"isAudit":"1"}
         */

        private boolean success;
        private DataBean data;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * isAudit : 1
             */

            private String isAudit;

            public String getIsAudit() {
                return isAudit;
            }

            public void setIsAudit(String isAudit) {
                this.isAudit = isAudit;
            }
        }
    }

    public static class AppAuditQueryFormBean {
        /**
         * appType : 0
         * marketCode : official
         * versionNumber : 1.0.0
         */

        private int appType;
        private String marketCode;
        private String versionNumber;

        public int getAppType() {
            return appType;
        }

        public void setAppType(int appType) {
            this.appType = appType;
        }

        public String getMarketCode() {
            return marketCode;
        }

        public void setMarketCode(String marketCode) {
            this.marketCode = marketCode;
        }

        public String getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(String versionNumber) {
            this.versionNumber = versionNumber;
        }
    }
}
