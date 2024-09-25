import "./MenuItem.css"
import placeholderImg from './antipasto-di-terra.jpg'

function MenuItem({ name, image, price, available }) {

    return (
        <div>
            <img src={image} alt='placeholder' className="item-image"></img>
            <div>{name}</div>
            <div>{available ? price + " €" : (<small>Not available</small>)}</div>
        </div>
    )
}

export default MenuItem;