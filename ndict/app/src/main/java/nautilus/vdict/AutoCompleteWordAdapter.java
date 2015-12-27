package nautilus.vdict;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import nautilus.vdict.data.VDictionary;

/**
 * Created by davu on 12/26/2015.
 */
public class AutoCompleteWordAdapter extends ArrayAdapter<String> implements Filterable {

    private static final List<String> EMPTY_LIST = new ArrayList<>();
    private VDictionary mDictionary;
    private List<String> mDataList;

    public AutoCompleteWordAdapter(Context context, int resId,
                                   int textviewResId,
                                   List<String> data,
                                   VDictionary dictionary) {
        super(context, resId, textviewResId, data);
        mDictionary = dictionary;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public String getItem(int pos) {
        return mDataList.get(pos);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        return super.getView(pos, convertView, parent);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults result = new FilterResults();
                List<String> words;
                if(constraint != null) {
                    words = mDictionary.initListWordString(constraint);
                } else {
                    words = new ArrayList<String>();
                }
                result.values = words;
                result.count = words.size();
                return result;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results.values != null) {
                    mDataList = (List<String>)results.values;
                } else {
                    mDataList = EMPTY_LIST;
                }

                if(results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;
    }
}
