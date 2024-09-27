import './App.css';
import MenuItem from './components/MenuItem';
import { useState, useEffect } from 'react';
import { menuItemsData, categoriesData } from './Data';

function App() {

  const [data, setData] = useState([]);
  const [categories, setCategories] = useState([]);
  const [currentCategory, setCurrentCategory] = useState(0);

  async function getMenu() {
    const menuData = await menuItemsData();

    menuData.forEach(item => {
      if (item.dish_image != null) {
        item.dish_image = item.dish_image.split(",").map(e => parseInt(e) >= 0 ? parseInt(e) : parseInt(e) + 256);
        item.dish_image = "data:img/png;base64," + btoa(String.fromCharCode.apply(null, item.dish_image));
      }
    });
    setData(menuData);
  }

  async function getCategories() {
    const categories = await categoriesData();
    setCategories(categories);
  }

  function handleCategoryClick(event, category) {
    const prev = document.getElementById('active');
    prev.setAttribute("id",null);
    prev.setAttribute("class","unactive");
    event.target.id = 'active';
    event.target.className = "active";
    setCurrentCategory(category);
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
      <div className='display-menu'>
        {data.map(el => {
            if (currentCategory === 0 || el["category"] === currentCategory) {
              return (
                <MenuItem
                  name={el["dish_name"]}
                  image={el["dish_image"]}
                  price={el["price"]}
                  available={el["available"]}
                  key={el["dish_id"]}
                />
              )
            } else {
              return null;
            }
        })}
      </div>
    </div>
  );
}

export default App;
