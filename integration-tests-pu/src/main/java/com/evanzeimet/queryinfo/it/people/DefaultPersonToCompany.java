package com.evanzeimet.queryinfo.it.people;

/*
 * #%L
 * queryinfo-integration-tests-pu
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 Evan Zeimet
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */



public class DefaultPersonToCompany implements PersonToCompany {

	private Long companyId;
	private Long id;
	private Long personId;

	public DefaultPersonToCompany() {

	}

	@Override
	public Long getCompanyId() {
		return companyId;
	}

	@Override
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getPersonId() {
		return personId;
	}

	@Override
	public void setPersonId(Long personId) {
		this.personId = personId;
	}

}
