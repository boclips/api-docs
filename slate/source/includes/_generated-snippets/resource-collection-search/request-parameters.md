Parameter | Description
--------- | -----------
`query` | A phrase you want to search by. Filters through collection titles
`discoverable` | By default only discoverable collections appear in search. To retrieve all collections use this property.
`promoted` | Whether you want to search through promoted collections only or not
`subject` | Allows to limit search results to specific subjects only
`age_range_min` | Minimum age to filter from - it filters on the collection age range property, and is inclusive
`age_range_max` | Maximum age to filter to - it filters on the collection age range property, and is inclusive
`age_range` | Filters on the video age ranges. Provide age ranges in the form `minAge-maxAge`, ie `5-7`. These ranges are inclusive.
`has_lesson_plans` | Allows to limit search results to collection with lesson plan attachment only
`page` | Index of search results page to retrieve
`size` | Collection page size
`projection` | Controls how sub-resources are fetched. Allowed values are `list` for shallow details and `details` for full sub-resource information. See <<resources-collections-projections,here>> for more details
`sort_by` | Sort collections by UPDATED_AT (last updated collections appear first), IS_DEFAULT (Watch later collections appear first), HAS_ATTACHMENT (collections with attachments appear first)
