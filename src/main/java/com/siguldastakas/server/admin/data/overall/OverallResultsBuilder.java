package com.siguldastakas.server.admin.data.overall;

import com.siguldastakas.server.admin.data.*;

import java.util.*;

public class OverallResultsBuilder {

   public static OverallResults build(Series series, EventResults[] eventResults) {

       Map<String, Map<String, OverallRunner>> classes = new HashMap<>();

       for (int e = 0; e < eventResults.length; e++) {
           EventResults event = eventResults[e];
           if (event == null) continue;

           for (RunnerClass eventClass : event.classes) {
               Map<String, OverallRunner> runners = classes.computeIfAbsent(eventClass.name, k -> new HashMap<>());
               for (Runner eventRunner : eventClass.runners) {
                   OverallRunner runner = runners.computeIfAbsent(eventRunner.name, k -> new OverallRunner(k, eventResults.length));
                   runner.club = eventRunner.club;
                   runner.eventPoints[e] = new EventPoints(eventRunner.points);
               }
           }
       }

       List<OverallClass> sortedClasses = new ArrayList<>(series.classes.length);
       for (SeriesClass seriesClass : series.classes) {
           Map<String, OverallRunner> runners = classes.get(seriesClass.name);
           if (runners == null) continue;

           List<OverallRunner> sortedRunners = new ArrayList<>(runners.values());
           for (OverallRunner runner : sortedRunners) {
               List<EventPoints> sortedPoints = new ArrayList<>(runner.eventPoints.length);
               for (EventPoints points : runner.eventPoints) {
                   if (points != null) sortedPoints.add(points);
               }
               if (sortedPoints.size() < seriesClass.overallResults) {
                   for (EventPoints points : sortedPoints) {
                       runner.points += points.points;
                       points.used = true;
                   }
               } else {
                   sortedPoints.sort(Comparator.comparing(o -> - o.points));
                   for (int p = 0; p < seriesClass.overallResults; p++) {
                       EventPoints points = sortedPoints.get(p);
                       runner.points += points.points;
                       points.used = true;
                   }
               }
           }
           sortedRunners.sort(Comparator.comparingInt(o -> - o.points));
           int position = 0;
           for (OverallRunner runner : sortedRunners) runner.position = ++position;

           sortedClasses.add(new OverallClass(seriesClass.name, sortedRunners.toArray(new OverallRunner[0])));
       }

       return new OverallResults(sortedClasses.toArray(new OverallClass[0]));
   }

}
