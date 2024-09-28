import "./MenuItem.css"
import { formatPriceString } from "../UtilityFunctions";

function MenuItem({ id, name, image, price, available, quantity, setQuantity }) {

    let priceString = formatPriceString(price);

    return (
        <div id={id} className="menu-item">
            <img src={image} alt={name + " thumbnail"} className="item-image"/>
            <div className="name">{name}</div>
            <br/>
            {available ? (<div>
                <button 
                    className="quantity-btn add"
                    onClick={(e) => setQuantity(e)}
                >+</button>
                {quantity > 0 
                ? (<button 
                    className="quantity-btn"
                    onClick={(e) => setQuantity(e)}
                >-</button>) 
                : ""}
            </div>) : ""}
            <div className="price">
                <small>{available ? priceString : "Not available"}</small>
            </div>
        </div>
    )
}

export default MenuItem;