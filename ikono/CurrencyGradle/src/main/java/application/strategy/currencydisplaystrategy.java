package application.strategy;

import application.Currency;

public interface CurrencyDisplayStrategy {
    String format(Currency currency);
}
