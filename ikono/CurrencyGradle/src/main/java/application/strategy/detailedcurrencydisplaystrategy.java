package application.strategy;

import application.Currency;
import java.math.BigDecimal;

public class DetailedCurrencyDisplayStrategy implements CurrencyDisplayStrategy {
    @Override
    public String format(Currency currency) {
        if (currency == null) return "";
        return String.format("ID: %s | Name: %s | Symbol: %s | Country: %s | Rate: %.4f | Status: %s | Decimals: %d",
                currency.getCurID(),
                currency.getCurName(),
                currency.getCurSymbol() != null ? currency.getCurSymbol() : "N/A",
                currency.getCurCountry() != null ? currency.getCurCountry() : "N/A",
                currency.getCurExcRate() != null ? currency.getCurExcRate() : BigDecimal.ZERO,
                currency.getCurStatus(),
                currency.getDecimalPlaces() != null ? currency.getDecimalPlaces() : 0
        );
    }
}
