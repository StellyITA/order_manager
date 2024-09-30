import "./OrderItem.css"
import editIcon from "./edit.png";
import okIcon from "./ok.png"
import { formatPriceString } from "../UtilityFunctions";
import { useState } from "react";

export default function OrderItem({ id, quantity, name, price, handleChange }) {
    
    const [isChanging, setChange] = useState(false);

    const priceString = formatPriceString(price * quantity);

    return (
        <p id={id} className="order-item">
            <button 
                className='order-item-edit'
                onClick={() => setChange(!isChanging)}
            ><img src={!isChanging ? editIcon : okIcon} alt="M"/></button>
            {!isChanging 
            ? (<span className="order-item-quantity">{quantity}</span>)
            : (<input 
                type="number" 
                defaultValue={quantity}
                className="order-item-quantity-input"
                onChange={(e) => handleChange(e)}
               />)} 
            <span className="order-item-name">{name}</span> 
            <span className="order-item-price">{priceString}</span>
            <button 
                className='order-item-delete'
                onClick={(e) => handleChange(e)}
            >X</button>
        </p>
    )
}