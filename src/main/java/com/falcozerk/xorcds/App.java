package com.falcozerk.xorcds;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {

    List<String> extList = Arrays.asList(new String[]{"mp3", "mp4", "m4a", "m4b", "m4v", "m4p", "mov", "aif"});

    Path sourcePath;
    Path targetPath;

    List<String> sourceList;
    List<String> targetList;

    public App(String pSourceDir, String pTargetDir) {
        sourcePath = Paths.get(pSourceDir);
        targetPath = Paths.get(pTargetDir);
    }

    public void run() {
        try {
            sourceList = getMusicPaths(sourcePath, sourcePath.toString());
            targetList = getMusicPaths(targetPath, targetPath.toString());

            List<String> uniqueAlbumList = getUniqueTargetAlbums();
            TreeMap<String, Artist> sourceTree = generateTrackTree(sourceList);
            TreeMap<String, Artist> targetTree = generateTrackTree(targetList);

            compareTrees(sourceTree, targetTree);

            System.out.println("Done.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    List<String> getUniqueTargetAlbums() {
        Set<String> sourceAlbumList = getAlbumList(sourceList);
        Set<String> targetAlbumList = getAlbumList(targetList);

        List<String> uniqueTargetAlbumList = new ArrayList<String>(targetAlbumList);
        uniqueTargetAlbumList.removeAll(sourceAlbumList);

        return uniqueTargetAlbumList;
    }

    public List<String> getMusicPaths(Path pPath, String pPathRoot) throws IOException {
        try (Stream<Path> stream = Files.walk(pPath)) {
            return stream
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(str -> extList.contains(str.substring(str.length() - 3)))
                    .filter(str -> str.length() > pPathRoot.length() + 1)
                    .map(str -> str.substring(pPathRoot.length() + 1))
                    .map(str -> str.replaceAll("[-_(),!~']*", ""))
                    .filter(str -> !str.startsWith("Movies"))
                    .sorted()
                    .collect(Collectors.toList());
        }
    }

    public Set<String> getAlbumList(List<String> pPathList) {
        try (Stream<String> stream = pPathList.stream()) {
            return stream
                    .map(str -> str.substring(0, str.lastIndexOf("/")))
                    .collect(Collectors.toSet());
        }
    }

    public TreeMap<String, Artist> generateTrackTree(List<String> pPath) {
        TreeMap<String, Artist> artistMap = new TreeMap<>();

        for (String path : pPath) {
            String[] pieces = path.split("/");
            if (pieces.length != 3) System.out.println("Bad path: " + path);
            else {
                Artist artist = addArtist(artistMap, pieces[0]);
                Album album = addAlbum(artist, pieces[1]);
                addTrack(album, pieces[2]);
            }


        }
        return artistMap;
    }

    public Artist addArtist(TreeMap<String, Artist> artistMap, String piece) {
        String artistName = piece;
        Artist artist = artistMap.get(artistName);
        if (artist == null) {
            artist = new Artist();
            artist.setName(artistName);
            artist.setPath(artistName + "/");

            artistMap.put(artistName, artist);
        }

        return artist;
    }

    public Album addAlbum(Artist pArtist, String pName) {
        Album album = pArtist.getAlbumMap().get(pName);
        if (album == null) {
            album = new Album();

            album.setName(pName);
            album.setPath(pName + "/");

            pArtist.getAlbumMap().put(pName, album);
        }

        return album;
    }

    public Track addTrack(Album pAlbum, String pName) {
        Track track = pAlbum.getTrackMap().get(pName);
        if (track == null) {
            track = new Track();
            pAlbum.getTrackMap().put(pName, track);

            track.setName(pName);
            track.setPath(pName + "/");
        }

        return track;
    }

    public void compareTrees(TreeMap<String, Artist> pTreeOne, TreeMap<String, Artist> pTreeTwo) {
        TreeMap<String,Artist> removeTree = new TreeMap<>();

        for (Map.Entry<String, Artist> treeOneEntry : pTreeOne.entrySet()) {
            for (Map.Entry<String, Artist> treeTwoEntry : pTreeOne.entrySet()) {
                Artist artistOne = treeOneEntry.getValue();
                Artist artistTwo = treeTwoEntry.getValue();

                if ( StringUtils.equals( artistOne.getName(), artistTwo.getName() ) ) {
                    System.out.println("Equal");
                }
            }
        }
    }
}
