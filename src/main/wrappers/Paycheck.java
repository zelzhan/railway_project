package main.wrappers;

public class Paycheck {
    String bankName;
    String amountPaid;
    String date;

    public Paycheck(String amount, String date) {
        this.amountPaid = amount;
        this.date = date;

        this.bankName = "Jysan Bank";
    }

}
