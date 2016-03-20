package com.scryp.dto;

import java.io.Serializable;

/**
 * Created by mayur.tailor on 06/11/2015.
 */
///{ "request":{ "methodName":"getCategories" }, "getCategories":{ "replyCode":"success", "replyMessage":"List successfully.", "response":[ { "isCurrent":"1", "term_id":"", "term_taxonomy_id":"", "name":"All", "slug":"All" }, { "isCurrent":"0", "term_id":"-99999", "term_taxonomy_id":"", "name":"Nearby", "slug":"Nearby" }, { "term_id":"44", "name":"B2B", "slug":"b2b", "term_group":"0", "term_order":"0", "term_taxonomy_id":"48", "taxonomy":"wg_category", "description":"B2B", "parent":"0", "count":"0", "isCurrent":"0" }, { "term_id":"3", "name":"Dining", "slug":"dining", "term_group":"0", "term_order":"2", "term_taxonomy_id":"3", "taxonomy":"wg_category", "description":"", "parent":"0", "count":"0", "isCurrent":"0" }, { "term_id":"8", "name":"Shopping", "slug":"shopping", "term_group":"0", "term_order":"3", "term_taxonomy_id":"8", "taxonomy":"wg_category", "description":"", "parent":"0", "count":"2", "isCurrent":"0" }, { "term_id":"2", "name":"Beauty", "slug":"beauty", "term_group":"0", "term_order":"4", "term_taxonomy_id":"2", "taxonomy":"wg_category", "description":"", "parent":"0", "count":"9", "isCurrent":"0" }, { "term_id":"4", "name":"Health", "slug":"health", "term_group":"0", "term_order":"5", "term_taxonomy_id":"4", "taxonomy":"wg_category", "description":"", "parent":"0", "count":"5", "isCurrent":"0" }, { "term_id":"6", "name":"Recreation", "slug":"activities", "term_group":"0", "term_order":"6", "term_taxonomy_id":"6", "taxonomy":"wg_category", "description":"", "parent":"0", "count":"0", "isCurrent":"0" }, { "term_id":"5", "name":"Learning", "slug":"learning", "term_group":"0", "term_order":"7", "term_taxonomy_id":"5", "taxonomy":"wg_category", "description":"", "parent":"0", "count":"0", "isCurrent":"0" }, { "term_id":"9", "name":"Travel", "slug":"travel", "term_group":"0", "term_order":"8", "term_taxonomy_id":"9", "taxonomy":"wg_category", "description":"", "parent":"0", "count":"0", "isCurrent":"0" }, { "term_id":"7", "name":"Services", "slug":"other", "term_group":"0", "term_order":"9", "term_taxonomy_id":"7", "taxonomy":"wg_category", "description":"", "parent":"0", "count":"0", "isCurrent":"0" } ] } }
public class CategoryDTO implements Serializable{

    public String term_id;
    public String name;
    public String slug;
    public String term_group;
    public String term_order;
    public String term_taxonomy_id;
    public String taxonomy;
    public String description;
    public String parent;
    public String count;
    public String isCurrent;

    public String getTerm_id() {
        return term_id;
    }

    public void setTerm_id(String term_id) {
        this.term_id = term_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTerm_group() {
        return term_group;
    }

    public void setTerm_group(String term_group) {
        this.term_group = term_group;
    }

    public String getTerm_order() {
        return term_order;
    }

    public void setTerm_order(String term_order) {
        this.term_order = term_order;
    }

    public String getTerm_taxonomy_id() {
        return term_taxonomy_id;
    }

    public void setTerm_taxonomy_id(String term_taxonomy_id) {
        this.term_taxonomy_id = term_taxonomy_id;
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(String isCurrent) {
        this.isCurrent = isCurrent;
    }
}
