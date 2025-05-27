package application.strategy;

import application.Currency;

public class SimpleCurrencyDisplayStrategy implements CurrencyDisplayStrategy {
    @Override
    public String format(Currency currency) {
        if (currency == null) return "";
        return String.format("ID: %s | Name: %s (%s)",
                currency.getCurID(),
                currency.getCurName(),
                currency.getCurStatus()
        );
    }
}
