Parameter | Description
--------- | -----------
`size` | The number of videos per page, 100 by default
`page` | Zero-index based page number, first page by default
`query` | The text search query
`duration` | Filters on the video duration property. Provide duration ranges in the form `min[-max]`, ie `PT1M-PT6M`. These ranges are inclusive. This property supersedes the duration_min and duration_max properties.
`duration_min` | Filters on the video duration property, this range is inclusive
`duration_max` | Filters on the video duration property, this range is inclusive
`released_date_from` | Filters on the video releasedOn property, this range is inclusive
`released_date_to` | Filters on the video releasedOn property, this range is inclusive
`updated_as_of` | Filters on the video updatedAt property, this range is inclusive
`source` | Filter by video source, e.g youtube or boclips
`subject` | Filter by subject id - from the <<resources-subjects,list of subjects>>
`education_level` | Filter by education level. Multiple values can be specified (comma separated, or by repeating the parameter). See possible values at <<resources-education-levels,education levels resource>>
`age_range_min` | Deprecated in favour of education_level. Minimum age to filter from - it filters on the video age range property, and is inclusive.
`age_range_max` | Deprecated in favour of education_level. Maximum age to filter to - it filters on the video age range property, and is inclusive
`age_range` | Deprecated in favour of education_level. Filter videos which cover at least 2 ages from a range in the video age range property.
`cefr_level` | Filter by CEFR level. Multiple values can be specified (comma separated, or by repeating the parameter). Possible values are: [A1, A2, B1, B2, C1, C2]
`duration_facets` | Override default facets for durations, see <<resources-video-search-facets,search facets>>.
`include_education_level_facets` | Indicates whether to include education level facets into search results.
`age_range_facets` | Deprecated. Override default facets for age ranges, see <<resources-video-search-facets,search facets>>.
`promoted` | Filter by promoted videos only
`content_partner` | Deprecated in favour of channel. Filter by content partner, which is the provider of the video content. Use multiple times to search for multiple values, e.g. 'content_partner=first&content_partner=second'.
`channel` | Filter by channel IDs (channel is the provider of the video content). Use multiple times to search for multiple values, e.g. 'channel=5d5432448256f68bdcf75d53&channel=5d77b49698cfe500017e9856'. Deprecated: filtering by channel names (it is still available, but will be removed anytime soon).
`include_channel_facets` | Indicates whether to include channel facets into search results.
`type` | Filter responses by <<resources-video-types,video type>>
`best_for` | Filter responses by <<resources-tags,tag>> labels
`sort_by` | A key to sort the results by, currently only release_date and rating are supported. This only sorts in a descending direction
`id` | Filter by video ids, this can be a comma separated list of video ids
`language` | Filter by language codes (ISO 639-2 language code). Use multiple times to search for multiple values, e.g. 'language=eng&language=spa'.
`ngss_code` | Filter by NGSS code. Multiple values can be specified (comma separated, or by repeating the parameter). See possible values at <<_retrieving_all_supported_ngss_codes,retrieving all NGSS codes>>
`ngss_grade` | Filter by NGSS grade. Multiple values can be specified (comma separated, or by repeating the parameter). See possible values at <<_retrieving_all_supported_ngss_grades,retrieving all NGSS grades>>
