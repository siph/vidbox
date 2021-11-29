package com.vidbox.album

import com.vidbox.file.File
import javax.persistence.*

@Entity
@Table(name = "albums")
class Album(@Id @GeneratedValue(strategy = GenerationType.AUTO)
            @Column(name = "id")
            val id: Long? = null,
            @Column(name = "owner")
            val owner: String,
            @ManyToMany(fetch = FetchType.EAGER)
            @JoinTable(
                name = "album_files",
                joinColumns = [JoinColumn(name = "album_id")],
                inverseJoinColumns = [JoinColumn(name = "file_id")])
            val files: MutableList<File> = ArrayList())
