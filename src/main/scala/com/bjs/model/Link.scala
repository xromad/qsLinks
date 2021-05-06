package com.bjs.model

import java.util.Date

final case class Link(
                       name: String,
                       url: String,
                       stars: Int,
                       dateAdded: Date,
                       dateModified: Date,
                       public: Boolean
                     )

