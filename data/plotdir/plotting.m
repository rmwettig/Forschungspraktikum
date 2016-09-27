clear; %clear workspace
clc; %clear command window
close; %close figures
fmt = 'C%d_%s_%s_%s_100000_rep_1,0er_bins.txt';
outfmt= 'C12%svsC14%s_%s';
meas = {'degDist', 'ClustCoeff', 'NeighbHConn', 'TopologCoeff'};
measAbbr = {'Deg', 'CC', 'NC', 'TC'};
gend = {'m', 'f'};
grp = {'gene', 'protein'};
grpCol = {'b', 'k'};
cons = [12, 14];
errorCheck = {};
for gIdx = 1:length(gend) %for all genders
    g = gend(gIdx);
    for grpIdx = 1:length(grp) %for both groups, gene and protein
        gr = grp(grpIdx);
        grpColIdx = 1;
        for cIdx = 1:length(cons) %for both consomic strains, 12 and 14
            c = cons(cIdx);
            plotNr = 1;
            for measIdx = 1:length(meas) %for all measures
                %plot order:
                % select a gender,
                % select a group,
                % select a strain,
                % plot all measures for this combination,
                % then select the next strain such that the comparison is
                % between (gender, group, 12, meas) 
                % and (gender, group, 14, meas)
                mea = meas(measIdx);
                file = sprintf(fmt,c,char(g),char(gr),char(mea));
                sprintf('Processing %s ...', file)
                %fid = fopen(file);
                %data = textscan(fid, '%f%d','Delimiter','\t', 'Headerlines', 1);
                clear('data'); %to assure that data is empty
                data = dlmread(file, '\t', 1, 0);
                %fclose(fid);
                [dummy, Idx] = sort(data(:,1));
                data = [data(Idx,1), data(Idx,2)];
                errorCheck = horzcat(errorCheck, data);
                subplot(2,2,plotNr);
                hold on;
                plot(data(:,1), data(:,2), char(grpCol(grpColIdx)));
                hold off;
                xlabel(measAbbr(measIdx));
                ylabel('Freq. of Sampled Average');
                plotNr = plotNr + 1;
            end
            grpColIdx = grpColIdx + 1;
        end
        legend('C12','C14');
        saveas(gcf,sprintf(outfmt, char(g), char(g), char(gr)), 'png');
        saveas(gcf,sprintf(outfmt, char(g), char(g), char(gr)), 'fig');
        close; %after saving plots for one group close to circumvent all plots in one figure
    end
end
%curve validation
%compares measures between one particular gender, one particular group and
%one particular consomic strain, e.g. mG12 and mG14
for ecol = 1:2:8
    first = errorCheck{ecol};
    second = errorCheck{ecol+1};
    for m = 1:4
        minIdx = min(size(first,1), size(second,1)); %only compare values up to the smallest length (row)
        first = first(1:minIdx,:);
        second = second(1:minIdx,:);
        err = first - second;
        err = sum(err==0);
        if(sum(err == 0) == 2)
            sprintf('Equal curves with indices: %d and %d at measure index %d', ecol, ecol+1, m);
        elseif(err(1) == 0)
            sprintf('Equal curves x values with indices: %d and %d at measure index %d', ecol, ecol+1, m);
        elseif(err(2) == 0)
            sprintf('Equal curves y values with indices: %d and %d at measure index %d', ecol, ecol+1, m);
        else
            'No duplicated curves found.'
        end
    end
end
'Error checking done.'