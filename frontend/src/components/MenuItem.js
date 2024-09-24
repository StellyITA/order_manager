import "./MenuItem.css"
import placeholderImg from './antipasto-di-terra.jpg'

function MenuItem({ name, price }) {

    return (
        <div>
            <img src={placeholderImg} alt='placeholder' className="item-image"></img>
            <div>{name}</div>
            <div>{price + " €"}</div>
        </div>
    )
}

export default MenuItem;