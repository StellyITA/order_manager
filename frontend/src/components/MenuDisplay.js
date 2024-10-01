import { useState } from "react";
import MenuItem from "./MenuItem";
import "./MenuDisplay.css"

export default function MenuDisplay({ loading, data, filterData, categories, setFilter, changeQuantity }) {

    const [currentCategory, setCurrentCategory] = useState(0);

    function handleCategoryClick(event, category) {
        const prev = document.getElementById('active');
        prev.removeAttribute("id");
        prev.setAttribute("class","unactive");
        event.target.id = 'active';
        event.target.className = "active";
        setCurrentCategory(category);
    }

    function onSearch(event) {
      const filterRegex = new RegExp(event.target.value, "i"); 
      setFilter(
        data.filter(
          el => el["dish_name"].match(filterRegex)
          || categories.filter(c => c.category_id === el.category)[0].category_name.match(filterRegex)
        )
      );
    }

    return (<section>
        <div className='input-wrapper'>
          <input 
            type='search' 
            className='search-menu'
            placeholder="Cerca per nome piatto o categoria..."
            onChange={e => onSearch(e)}
          />
        </div>
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
      
        {loading ? <div className="loader"/> : (<div className='display-menu'>
          {filterData.map(el => {
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
    </section>)
}