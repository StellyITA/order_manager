export function formatPriceString(price) {
    let priceString = price.toString();
    
    if (priceString.match(/\.\d$/) != null) {

        priceString += "0 €";

    } else if (priceString.match(/\.\d{3,}/) != null) {

        let priceArr = priceString.split("");
        let i = 0;

        while (priceArr[i] !== ".") {
            i++;
        }
        i += 3;

        priceArr.splice(i);
        priceString = priceArr.join("") + " €";

    } else if (priceString.match(/\./) == null) {

        priceString += ".00 €";

    } else {
        
        priceString += " €";
    }

    return priceString;
}