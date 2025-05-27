package application.decorator;

import application.Currency;
import application.service.CurrencyService;

import java.util.List;

public class LoggingCurrencyServiceDecorator implements CurrencyService {
    private final CurrencyService wrappedService;

    public LoggingCurrencyServiceDecorator(CurrencyService wrappedService) {
        this.wrappedService = wrappedService;
    }

    private void log(String action, String details) {
        System.out.println("[LOG] Action: " + action + " | Details: " + details + " | Timestamp: " + java.time.LocalDateTime.now());
    }

    @Override
    public List<Currency> getAllCurrencies() {
        log("GetAllCurrencies", "Fetching all currencies");
        List<Currency> result = wrappedService.getAllCurrencies();
        log("GetAllCurrencies", "Found " + result.size() + " currencies");
        return result;
    }

    @Override
    public Currency findCurrencyById(String curID) {
        log("FindCurrencyById", "Searching for currency ID: " + curID);
        Currency result = wrappedService.findCurrencyById(curID);
        log("FindCurrencyById", result != null ? "Found: " + curID : "Not found: " + curID);
        return result;
    }

    @Override
    public boolean addCurrency(Currency currency) {
        log("AddCurrency", "Attempting to add currency: " + currency.getCurID());
        boolean success = wrappedService.addCurrency(currency);
        log("AddCurrency", "Result for " + currency.getCurID() + ": " + (success ? "Success" : "Failed"));
        return success;
    }

    @Override
    public boolean updateCurrency(Currency currency) {
        log("UpdateCurrency", "Attempting to update currency: " + currency.getCurID());
        boolean success = wrappedService.updateCurrency(currency);
        log("UpdateCurrency", "Result for " + currency.getCurID() + ": " + (success ? "Success" : "Failed"));
        return success;
    }

    @Override
    public boolean deleteCurrency(String curID) {
        log("DeleteCurrency", "Attempting to delete currency ID: " + curID);
        boolean success = wrappedService.deleteCurrency(curID);
        log("DeleteCurrency", "Result for " + curID + ": " + (success ? "Success" : "Failed"));
        return success;
    }
}
