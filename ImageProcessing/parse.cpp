#include <iostream>
#include <fstream>
#include <string>
#include <vector>

using namespace std;

int LevenshteinDistance(string s, string t)
{
    // degenerate cases
    if (s == t) return 0;
    if (s.size() == 0) return t.size();
    if (t.size() == 0) return s.size();

    // create two work vectors of integer distances
    vector<int> v0(t.size() + 1);
    vector<int> v1(t.size() + 1);

    // initialize v0 (the previous row of distances)
    // this row is A[0][i]: edit distance for an empty s
    // the distance is just the number of characters to delete from t
    for (int i = 0; i < v0.size(); i++)
        v0[i] = i;

    for (int i = 0; i < s.size(); i++)
    {
        // calculate v1 (current row distances) from the previous row v0

        // first element of v1 is A[i+1][0]
        //   edit distance is delete (i+1) chars from s to match empty t
        v1[0] = i + 1;

        // use formula to fill in the rest of the row
        for (int j = 0; j < t.size(); j++)
        {
            int cost = (s[i] == t[j]) ? 0 : 1;
            v1[j + 1] = min(min(v1[j] + 1, v0[j + 1] + 1), v0[j] + cost);
        }

        // copy v1 (current row) to v0 (previous row) for next iteration
        for (int j = 0; j < v0.size(); j++)
            v0[j] = v1[j];
    }

    return v1[t.size()];
}

int main(int argc, char** argv) {

    if(argc != 4) {
        cerr << "Usage: ./parse ocr_file.txt check_data.txt result.txt\n";
        return 1;
    }


    string num;
    string name;

    vector<string> lines;
    int lineNum;
    ifstream in_ocr(argv[1]);

    while(!in_ocr.eof()) {
        string line;
        getline(in_ocr, line);

        if(!in_ocr) {
            break;
        }

        ++lineNum;
        for(int i = 0; i < line.size(); ++i) {
            if(isdigit(line[i])) {
                num += line[i];
            }
        }

        bool hasAlpha = false;
        for(int i = 0; i < line.size(); ++i) {
            if(isalpha(line[i])) {
                hasAlpha = true;
                break;
            }
        }

        if(hasAlpha) {
            lines.push_back(line);
        }
    }

    for(int i = lines.size() - 2; i < lines.size(); ++i) {
        string line = lines[i];
        for(int j = 0; j < line.size(); ++j) {
            if(isalpha(line[j])) {
                name += tolower(line[j]);
            }
        }
    }

    cout << num << name << "\n";

    string query = num + name;

    vector<string> data_entries;
    ifstream in_check(argv[2]);

    while(!in_check.eof()) {
        string line;
        getline(in_check, line);

        if(!in_check) {
            break;
        }

        string entry;
        for(int i = 0; i < line.size(); ++i) {
            if(isdigit(line[i])) {
                entry += line[i];
            }

            if(isalpha(line[i])) {
                entry += tolower(line[i]);
            }
        }

        data_entries.push_back(entry);
    }

    int res = 100000;
    string ans = "";
    for(int i = 0; i < data_entries.size(); ++i) {
        int dist = LevenshteinDistance(query, data_entries[i]);
        if( dist < res) {
            res = dist;
            ans = data_entries[i];
        }
    }

    ofstream out(argv[3]);

    out << res << "\n";
    out << ans << "\n";

    return 0;

}
