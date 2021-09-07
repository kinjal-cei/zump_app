package com.catch32.zumpbeta.listener;

/**
 * @author Ruchi Mehta
 * @version Jul 22, 2019
 */
public interface OrderDetailsSubmitListener {

    public enum PAY_TERM {
        ON_DELIVERY("On Delivery"),
        ON_NEXT_CYCLE("On Vendor Choice");

        public final String label;

        private PAY_TERM(String label) {
            this.label = label;
        }


        public String getPayTerm() {
            return this.label;
        }
    }

    void onPayTermSelected(PAY_TERM payTerm);

}
