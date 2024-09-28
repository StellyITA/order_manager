import "./MenuItem.css"

function MenuItem({ name, image, price, available }) {

    let priceString = price.toString();
    
    if (priceString.match(/\.\d$/) != null) {
        priceString += "0 €";
    } else if (priceString.match(/\./) == null) {
        priceString += ".00 €";
    } else {
        priceString += " €";
    }

    return (
        <div className="menu-item">
            <img src={image} alt={name + " thumbnail"} className="item-image"></img>
            <div className="name">{name}</div>
            <br/>
            <div className="price">
                <small>{available ? priceString : "Not available"}</small>
            </div>
        </div>
    )
}

export default MenuItem;