export function formatPriceString(price) {
    let priceString = price.toString();
    
    if (priceString.match(/\.\d$/) != null) {
        priceString += "0 €";
    } else if (priceString.match(/\./) == null) {
        priceString += ".00 €";
    } else {
        priceString += " €";
    }

    return priceString
}