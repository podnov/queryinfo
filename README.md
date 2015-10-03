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