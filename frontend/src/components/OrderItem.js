import { formatPriceString } from "../UtilityFunctions";

export default function OrderItem({ id, quantity, name, price }) {
    
    const priceString = formatPriceString(price);

    return (
        <p id={id}><span>{quantity}</span> <span>{name}</span> <span>{priceString}</span></p>
    )
}