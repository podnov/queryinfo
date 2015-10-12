# QueryInfo

The purpose of this code is to have a Java api for creating a serializable query structure. A JSON example of this structure looks like so:

```javascript
{
  "conditionGroup" : {
    "conditionGroups" : [
      {
        "conditionGroups" : [ ],
        "conditions" : [
          {
            "leftHandSide" : "category",
            "operator" : "=",
            "rightHandSide" : "Awesome Stuff"
          },
          {
            "leftHandSide" : "priority",
            "operator" : ">",
            "rightHandSide" : "2"
          }
        ],
        "operator" : "or"
      }
    ],
    "conditions" : [
      {
        "leftHandSide" : "active",
        "operator" : "=",
        "rightHandSide" : "true"
      }
    ],
    "operator" : "and"
  },
  "paginationInfo" : {
    "pageIndex" : 0,
    "pageSize" : 9223372036854775807
  },
  "requestedFieldNames" : [
    "a",
    "b",
    "c"
  ],
  "sorts" : [
    {
      "direction" : "asc",
      "fieldName" : "x"
    }
  ]
}
```

Using the queryinfo-jpa module, you can map condition field names (lefHandSide) to JPA entity attributes using annotations.  The framework can create and execute a JPA CriteriaQuery based on your QueryInfo.

In the integration-tests module, see the [CompanyEntity](integration-tests-pu/src/main/java/com/evanzeimet/queryinfo/it/companies/CompanyEntity.java) and [Conditions Cucumber Feature File](integration-tests/src/test/resources/com/evanzeimet/queryinfo/it/conditions/conditions.feature) for an example.