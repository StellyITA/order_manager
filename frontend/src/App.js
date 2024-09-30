import './App.css';

import OrderItem from './components/OrderItem';
import MenuItem from './components/MenuItem';
import { useState, useEffect } from 'react';
import { menuItemsData, categoriesData } from './Data';
import { formatPriceString } from './UtilityFunctions';

function App() {

  const [loading, setLoading] = useState(true);
  const [data, setData] = useState([]);
  const [orderData, setOrderData] = useState([]);
  const [categories, setCategories] = useState([]);
  const [currentCategory, setCurrentCategory] = useState(0);

  async function getMenu() {
    const menuData = await menuItemsData();

    menuData.forEach(item => {
      if (item.dish_image != null) {
        item.dish_image = item.dish_image.split(",").map(e => parseInt(e) >= 0 ? parseInt(e) : parseInt(e) + 256);
        item.dish_image = "data:img/png;base64," + btoa(String.fromCharCode.apply(null, item.dish_image));
      }
      item.quantity = 0;
    });
    setData(menuData);
    setLoading(false);
  }

  async function getCategories() {
    const categories = await categoriesData();
    setCategories(categories);
  }

  function handleCategoryClick(event, category) {
    const prev = document.getElementById('active');
    prev.removeAttribute("id");
    prev.setAttribute("class","unactive");
    event.target.id = 'active';
    event.target.className = "active";
    setCurrentCategory(category);
  }

  function changeQuantity(event) {
    const id = event.target.parentNode.parentNode.id;
    let newData = JSON.parse(JSON.stringify(data));

    let i = 0;
    while(i < newData.length && newData[i]["dish_id"] !== parseInt(id)) {
      i++;
    }

    if (event.target.className.match("add") !== null) {
      newData[i]["quantity"]++;
    } else {
      newData[i]["quantity"]--;
    }

    setData(newData);
    setOrderData(newData.filter(el => el["quantity"] > 0));
  }

  useEffect(() => {
    getMenu();
    getCategories();
  },[]);

  return (
    <div className="App">
      <header className="App-header">
        Header
      </header>
      <div className='categories'>
        <button 
          id='active'
          className='active'
          key='All0'
          onClick={(e) => handleCategoryClick(e,0)}
        >Tutte le categorie</button>
        {categories.map(el => {
          return (
            <button 
              className='unactive'
              key={el["category_name"] + el["category_id"]}
              onClick={(e) => handleCategoryClick(e,el["category_id"])}
            >{el["category_name"]}</button>
          )
        })}
      </div>
      <div className='order-manager'>
        {loading ? <div className="loader"/> : (<div className='display-menu'>
          {data.map(el => {
              if (currentCategory === 0 || el["category"] === currentCategory) {
                return (
                  <MenuItem
                    id={el["dish_id"]}
                    name={el["dish_name"]}
                    image={el["dish_image"]}
                    price={el["price"]}
                    available={el["available"]}
                    quantity={el["quantity"]}
                    key={el["dish_id"]}
                    setQuantity={changeQuantity}
                  />
                )
              } else {
                return null;
              }
          })}
        </div>)}
        <div id='order' className='order'>
          Ordine
          {orderData.map(el => {
            return (
              <OrderItem
                id={el["dish_id"]}
                quantity={el["quantity"]}
                name={el["dish_name"]}
                price={el["price"]}
                key={el["dish_name"] + el["dish_id"]}
              />
            )
          })}
          <p className="total">
            <span className="total-text">Total:</span>
            <span className="total-price">{(orderData.length > 0) 
            ? formatPriceString(orderData.reduce((acc,current) => 
              acc + current["price"] * current["quantity"], 0)) 
            : ""}</span>
            </p>
        </div>
      </div>
    </div>
  );
}

export default App;
