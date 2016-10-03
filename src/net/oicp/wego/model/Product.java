package net.oicp.wego.model;

import java.util.List;

import net.oicp.wego.annotations.EKey;
import net.oicp.wego.annotations.Index;
import net.oicp.wego.model.append.Festival;
import net.oicp.wego.model.base.Base;

/** 
 * @author         lolog
 * @version        V1.0  
 * @date           2016.08.02
 * @company        lolog
 * @description    product 
*/
@EKey("$set")
public class Product extends Base {
	@Index(unique=true,name="product_id")
	private String product_id;
	@Index(unique=true)
	private String name;
	private String title;
	private List<String> banner;
	private String classify_id;
	private String classify_name;
	private String brand_id;
	private String brand_name;
	private String currency_id;
	private String currency_name;
	private Double currency_rate;
	private Double currency_price;
	private Double price;
	private Integer stock;
	private Integer count;
	private Integer cost_count;
	private String description;
	private Double discount;
	private List<Festival> festivals;
	private List<String> keywords;
	private List<String> labels;
	private List<Shop> shops;
	private Boolean is_recommand;
	private Integer recommand_level;
	
	public Product() {
		
	}
	
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<String> getBanner() {
		return banner;
	}
	public void setBanner(List<String> banner) {
		this.banner = banner;
	}
	public String getClassify_id() {
		return classify_id;
	}
	public void setClassify_id(String classify_id) {
		this.classify_id = classify_id;
	}
	public String getClassify_name() {
		return classify_name;
	}
	public void setClassify_name(String classify_name) {
		this.classify_name = classify_name;
	}
	public String getBrand_id() {
		return brand_id;
	}
	public void setBrand_id(String brand_id) {
		this.brand_id = brand_id;
	}
	public String getBrand_name() {
		return brand_name;
	}
	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}
	public String getCurrency_id() {
		return currency_id;
	}
	public void setCurrency_id(String currency_id) {
		this.currency_id = currency_id;
	}
	public String getCurrency_name() {
		return currency_name;
	}
	public void setCurrency_name(String currency_name) {
		this.currency_name = currency_name;
	}
	public Double getCurrency_rate() {
		return currency_rate;
	}
	public void setCurrency_rate(Double currency_rate) {
		this.currency_rate = currency_rate;
	}
	public Double getCurrency_price() {
		return currency_price;
	}
	public void setCurrency_price(Double currency_price) {
		this.currency_price = currency_price;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getCost_count() {
		return cost_count;
	}
	public void setCost_count(Integer cost_count) {
		this.cost_count = cost_count;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public List<Festival> getFestivals() {
		return festivals;
	}
	public void setFestivals(List<Festival> festivals) {
		this.festivals = festivals;
	}
	public List<String> getKeywords() {
		return keywords;
	}
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	public List<String> getLabels() {
		return labels;
	}
	public void setLabels(List<String> labels) {
		this.labels = labels;
	}
	public List<Shop> getShops() {
		return shops;
	}
	public void setShops(List<Shop> shops) {
		this.shops = shops;
	}
	public Boolean getIs_recommand() {
		return is_recommand;
	}
	public void setIs_recommand(Boolean is_recommand) {
		this.is_recommand = is_recommand;
	}
	public Integer getRecommand_level() {
		return recommand_level;
	}
	public void setRecommand_level(Integer recommand_level) {
		this.recommand_level = recommand_level;
	}

	@Override
	public String toString() {
		return "Product [product_id=" + product_id + ", name=" + name + ", title=" + title + ", banner=" + banner
				+ ", classify_id=" + classify_id + ", classify_name=" + classify_name + ", brand_id=" + brand_id
				+ ", brand_name=" + brand_name + ", currency_id=" + currency_id + ", currency_name=" + currency_name
				+ ", currency_rate=" + currency_rate + ", currency_price=" + currency_price + ", price=" + price
				+ ", stock=" + stock + ", count=" + count + ", cost_count=" + cost_count + ", description="
				+ description + ", discount=" + discount + ", festivals=" + festivals + ", keywords=" + keywords
				+ ", labels=" + labels + ", shops=" + shops + ", is_recommand=" + is_recommand + ", recommend_level="
				+ recommand_level + " Base [id=" + getId() + ", addy_id=" + getAddy_id() + ", addy_name=" + getAddy_name() 
				+ ", add_time=" + getAdd_time() + ", editors=" + getEditors() + ", is_del=" + getIs_del() + ", is_forbidden=" 
				+ getIs_forbidden() + "]]";
	}
}