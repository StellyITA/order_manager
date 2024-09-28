import "./OrderItem.css"
import { formatPriceString } from "../UtilityFunctions";

export default function OrderItem({ id, quantity, name, price }) {
    
    const priceString = formatPriceString(price * quantity);

    return (
        <p id={id} className="order-item">
            <span className="order-item-quantity">{quantity}</span> 
            <span className="order-item-name">{name}</span> 
            <span className="order-item-price">{priceString}</span>
        </p>
    )
}