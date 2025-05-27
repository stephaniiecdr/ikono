package application.service;

import application.Currency;
import java.util.List;

public interface CurrencyService {
    List<Currency> getAllCurrencies();
    Currency findCurrencyById(String curID);
    boolean addCurrency(Currency currency);
    boolean updateCurrency(Currency currency);
    boolean deleteCurrency(String curID);
}
