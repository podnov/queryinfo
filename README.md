# QueryInfo

![Build Status](https://travis-ci.org/podnov/queryinfo.svg?branch=master)

The purpose of this code is to easily create and execute dynamic queries against arbitrary data. The queryinfo-common module includes POJOs that allow you create a serializable query structure. A JSON example of this structure looks like so:

```javascript
{
	"conditionGroup": {
		"conditionGroups": [
			{
				"conditions": [
					{
						"leftHandSide": "state",
						"operator": "=",
						"rightHandSide": "CA"
					},
					{
						"leftHandSide": "state",
						"operator": "=",
						"rightHandSide": "WA"
					}
				],
				"operator": "or"
			}
		],
		"conditions": [
			{
				"leftHandSide": "active",
				"operator": "=",
				"rightHandSide": true
			}
		],
		"operator": "and"
	},
	"paginationInfo" : {
		"pageIndex" : 0,
		"pageSize" : 9223372036854775807
	},
	"requestedFields" : [
		"name",
		"address",
		"city",
		"state"
	],
	"sorts" : [
		{
			"direction" : "desc",
			"fieldName" : "name"
		}
	]
}
```

Using the queryinfo-jpa module, you can map condition field names ("leftHandSide") to JPA entity attributes using custom annotations. The framework can create and execute a [JPA CriteriaQuery](http://docs.oracle.com/javaee/6/tutorial/doc/gjitv.html) based on your supplied QueryInfo structure.

#### Given this PersonEntity:

```java
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.evanzeimet.queryinfo.it.organizations.OrganizationEntity;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoField;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoin;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinType;

@Entity
@Table(name = "people")
public class PersonEntity extends DefaultPerson {
	
	private OrganizationEntity employerEntity;
	
	@QueryInfoJoin(name = "employer",
		joinType = QueryInfoJoinType.LEFT)
	@ManyToOne
	@JoinColumn(name = "employer_organization_id",
			referencedColumnName = "id",
			insertable = false,
			updatable = false)
	public OrganizationEntity getEmployerEntity() {
		return employerEntity;
	}
	
	public void setEmployerEntity(OrganizationEntity employerEntity) {
		this.employerEntity = employerEntity;
	}
	
	@Override
	@Column(name = "employer_organization_id")
	public Long getEmployerOrganizationId() {
		return super.getEmployerOrganizationId();
	}
	
	@Override
	@QueryInfoField
	@Column(name = "first_name")
	public String getFirstName() {
		return super.getFirstName();
	}
	
	@Override
	@QueryInfoField
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Long getId() {
		return super.getId();
	}
	
	@Override
	@QueryInfoField
	@Column(name = "last_name")
	public String getLastName() {
		return super.getLastName();
	}
}
```

#### And this OrganizationEntity:

```java
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.evanzeimet.queryinfo.it.people.PersonEntity;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoField;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoin;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinType;

@Entity
@Table(name = "organizations")
public class OrganizationEntity extends DefaultOrganization {

	private List<PersonEntity> employeeEntities;

	@Override
	@QueryInfoField
	@Column(name = "active")
	public Boolean getActive() {
		return super.getActive();
	}

	@Override
	@QueryInfoField
	@Column(name = "address1")
	public String getAddress1() {
		return super.getAddress1();
	}

	@Override
	@QueryInfoField
	@Column(name = "address2")
	public String getAddress2() {
		return super.getAddress2();
	}

	@Override
	@QueryInfoField
	@Column(name = "city")
	public String getCity() {
		return super.getCity();
	}

	@Override
	@QueryInfoField
	@Column(name = "date_created")
	public Date getDateCreated() {
		return super.getDateCreated();
	}

	@QueryInfoJoin(name = "employees",
		joinType = QueryInfoJoinType.LEFT)
	@OneToMany(mappedBy = "employerEntity")
	public List<PersonEntity> getEmployeeEntities() {
		return employeeEntities;
	}

	public void setEmployeeEntities(List<PersonEntity> employeeEntities) {
		this.employeeEntities = employeeEntities;
	}

	@Override
	@QueryInfoField
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Long getId() {
		return super.getId();
	}

	@Override
	@QueryInfoField
	@Column(name = "name")
	public String getName() {
		return super.getName();
	}

	@Override
	@QueryInfoField
	@Column(name = "state")
	public String getState() {
		return super.getState();
	}

	@Override
	@QueryInfoField
	@Column(name = "year_founded")
	public Integer getYearFounded() {
		return super.getYearFounded();
	}

	@Override
	@QueryInfoField
	@Column(name = "zip")
	public String getZip() {
		return super.getZip();
	}
}
```

#### And these organizations:

```
| name      | address1                  | address2 | city          | state | zip   | yearFounded | active | dateCreated         |
| CDW       | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        | true   | 2015-10-09T00:00:00 |
| Google    | 1600 Amphitheatre Parkway |          | Mountain View | CA    | 94043 | 1998        | true   | 2015-10-10T00:00:00 |
| Epic      | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        | true   | 2015-10-11T00:00:00 |
| Pets.com  |                           |          | San Francisco | CA    | 94101 | 1998        | false  | 2015-10-12T00:00:00 |
| Amazon    | 410 Terry Ave. N          |          | Seattle       | WA    | 98109 | 1994        | true   | 2015-10-13T00:00:00 |
| Facebook  | 1 Hacker Way              |          | Menlo Park    | CA    | 94025 | 2004        | true   | 2015-10-14T00:00:00 |
| U.S. Navy | The Pentagon              |          | Washington    | DC    | 20001 | 1775        | true   | 2015-10-15T00:00:00 |
```

#### And these people:

```
| firstName | lastName   | employerOrganizationName |
| Evan      | Zeimet     | CDW                      |
| Larry     | Page       | Google                   |
| Judith    | Faulkner   | Epic                     |
| Jeff      | Bezos      | Amazon                   |
| Mark      | Zuckerberg | Facebook                 |
| Pete      | Mitchell   | U.S. Navy                |
| Nick      | Bradshaw   | U.S. Navy                |
| Tom       | Kazanski   | U.S. Navy                |
| Mike      | Metcalf    | U.S. Navy                |
```

#### When I submit this query for organization employees:

```
{
	"conditionGroup": {
		"conditions": [
			{
				"leftHandSide": "state",
				"operator": "not in",
				"rightHandSide": ["WA","CA"]
			}
		]
	},
	"requestedFields": [
		"name",
		"state",
		"employees.firstName",
		"employees.lastName"
	],
	"sorts":[
		{
			"fieldName": "name",
			"direction": "desc"
		},
		{
			"fieldName": "employees.lastName",
			"direction": "asc"
		}
	]
}
```

#### Then I should receive these results:

```
| name          | state   | employees.firstName | employees.lastName |
| U.S. Navy     | DC      | Nick                | Bradshaw           |
| U.S. Navy     | DC      | Tom                 | Kazanski           |
| U.S. Navy     | DC      | Mike                | Metcalf            |
| U.S. Navy     | DC      | Pete                | Mitchell           |
| Epic          | WI      | Judith              | Faulkner           |
| CDW           | IL      | Evan                | Zeimet             |
```

In the integration-tests module, see the [Cucumber Feature File](integration-tests/src/test/resources/com/evanzeimet/queryinfo/it/feature/queryinfo.feature) for more query examples.


## The Glue

### Entities

The default way to declare the fields on JPA entities that are in-scope for querying is by using annotations. The annotations are [@QueryInfoField](jpa/src/main/java/com/evanzeimet/queryinfo/jpa/field/QueryInfoField.java) and [@QueryInfoJoin](jpa/src/main/java/com/evanzeimet/queryinfo/jpa/join/QueryInfoJoin.java). If these annotations exist on an entity field, the field is considered in-scope for various purposes. Using the [@QueryInfoField](jpa/src/main/java/com/evanzeimet/queryinfo/jpa/field/QueryInfoField.java) annotation, you can declare that a field is selectable, orderable, and/or predicateable. This means that for any field, you can independently toggle whether it is returned with query results, whether you can sort on that field, and whether you can create predicates for that field. Using the [@QueryInfoJoin](jpa/src/main/java/com/evanzeimet/queryinfo/jpa/join/QueryInfoJoin.java) annotation, you can specify that queries are allowed to walk joined entities and how that join should be performed (LEFT, RIGHT, INNER).

### QueryInfoEntityContext

[QueryInfoEntityContext](jpa/src/main/java/com/evanzeimet/queryinfo/jpa/entity/QueryInfoEntityContext.java)s contain the runtime information needed to execute queries against a single entity. This information consists of a reference to the JPA entity class, a [QueryInfoPathFactory](jpa/src/main/java/com/evanzeimet/queryinfo/jpa/path/QueryInfoPathFactory.java) that can create JPA paths for QueryInfo field names, and a [QueryInfoAttributeContext](jpa/src/main/java/com/evanzeimet/queryinfo/jpa/attribute/QueryInfoAttributeContext.java) that contains information about the QueryInfo fields and joins that exist on an entity.

### QueryInfoEntityContextRegistry

Entity resolution and walking is enabled by a [QueryInfoEntityContextRegistry](jpa/src/main/java/com/evanzeimet/queryinfo/jpa/entity/QueryInfoEntityContextRegistry.java). This registry contains [QueryInfoEntityContext](jpa/src/main/java/com/evanzeimet/queryinfo/jpa/entity/QueryInfoEntityContext.java)s for each entity that's in-scope for the QueryInfo environment and allows queries for one entity to find the appropriate contexts for joined entities.

### QueryInfoBean

JPA criteria query execution is performed by a query bean. The [AbstractQueryInfoBean](jpa/src/main/java/com/evanzeimet/queryinfo/jpa/bean/AbstractQueryInfoBean.java) is the heavy-lifter that glues all the parts together. There are convenience generic versions depending on how you want to deserialize your query results. The [DefaultEntityQueryInfoBean](jpa/src/main/java/com/evanzeimet/queryinfo/jpa/bean/entity/DefaultEntityQueryInfoBean.java) is useful when you're querying an entity and want instances of that entity returned. The [DefaultTupleQueryInfoBean](jpa/src/main/java/com/evanzeimet/queryinfo/jpa/bean/tuple/DefaultTupleQueryInfoBean.java) is useful when you need to do some sort of custom tuple deserialization.

### QueryInfoBeanContext

A [QueryInfoBeanContext](jpa/src/main/java/com/evanzeimet/queryinfo/jpa/bean/QueryInfoBeanContext.java) is a container for the parts (or part factories) that need to be glued together. QueryInfoBeanContexts contain all of the queryinfo state necessary for the QueryInfoBean to perform a query and return results.
