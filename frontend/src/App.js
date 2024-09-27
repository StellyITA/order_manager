import './App.css';
import MenuItem from './components/MenuItem';
import { useState, useEffect } from 'react';
import axios from 'axios';

function App() {

  const [data, setData] = useState([]);
  const [categories, setCategories] = useState([]);

  useEffect(() => {

    // GET menu items
    axios.get("http://localhost:8080/menu")
         .then((response) => {

          let jsonRes = response.data;

          jsonRes.forEach(item => {
            if (item.dish_image != null) {
              item.dish_image = item.dish_image.split(",").map(e => parseInt(e) >= 0 ? parseInt(e) : parseInt(e) + 256);
              item.dish_image = "data:img/png;base64," + btoa(String.fromCharCode.apply(null, item.dish_image));
            }
          });
          setData(jsonRes);
         })
         .catch(err => {
          console.log(err);
         })

    // GET categories
    axios.get("http://localhost:8080/menu/categories")
         .then((response) => {
          setCategories(response.data);
         })
         .catch(err => {
          console.log(err);
         })
  },[]);

  return (
    <div className="App">
      <header className="App-header">
        Header
      </header>
      <div>
        <button>Tutte le categorie</button>
        {categories.map(el => {
          return (
            <button>{el.category_name}</button>
          )
        })}
      </div>
      <div className='display-menu'>
      {data.map(el => {
          return (
            <MenuItem
              name={el["dish_name"]}
              image={el["dish_image"]}
              price={el["price"]}
              available={el["available"]}
              key={el["dish_id"]}
            />
          )
        })}
      </div>
    </div>
  );
}

export default App;
