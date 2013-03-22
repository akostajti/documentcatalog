/*
 * Copyright by Akos Tajti (akos.tajti@gmail.com)
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Akos Tajti. ("Confidential Information"). You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Akos Tajti.
 */
package net.docca.backend.web.controllers;

import net.docca.backend.search.AbstractSearchProxy;
import net.docca.backend.search.DefaultSearchExpression;
import net.docca.backend.search.ProxyTypes;
import net.docca.backend.search.SearchException;
import net.docca.backend.search.SearchProxy;
import net.docca.backend.search.SearchResult;
import net.docca.backend.web.controllers.forms.SearchForm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * handles the search related requests.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Controller
@RequestMapping("/search")
public class SearchController {

	@ModelAttribute("searchForm")
	public SearchForm getForm() {
		return new SearchForm();
	}

	/**
	 * shows the search form.
	 * @return the name of the search form jsp.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String show() {
		return "search/search";
	}

	/**
	 * executes the search based on the keyword passed in the request.
	 * @param form the form wrapping the request parameters
	 * @param model the model
	 * @return the name of the search result jsp
	 * @throws SearchException thrown on any error during the search
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String search(@ModelAttribute("searchForm") final SearchForm form, final Model model)
			throws SearchException {
		SearchProxy proxy = AbstractSearchProxy.getSearchProxyForType(ProxyTypes.lucene);

		// create a siple search expression from the keyword
		SearchResult result = proxy.find(new DefaultSearchExpression(form.getKeyword()));
		model.addAttribute("result", result);

		return "search/result";
	}
}

