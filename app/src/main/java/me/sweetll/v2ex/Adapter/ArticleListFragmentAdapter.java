package me.sweetll.v2ex.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

import me.sweetll.v2ex.Fragment.ArticleListFragment;

/**
 * Created by sweet on 15-8-17.
 */
public class ArticleListFragmentAdapter extends FragmentPagerAdapter {
    private Map<Integer, ArticleListFragment> fragmentMap = new HashMap<>();

    private String tabTitles[] = new String[] { "技术", "创意", "好玩", "Apple", "酷工作", "交易", "城市", "问与答", "最热", "全部", "R2" };
    final int PAGE_COUNT = tabTitles.length;

    public ArticleListFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        ArticleListFragment fragment = ArticleListFragment.newInstance(position + 1);
        fragmentMap.put(position, fragment);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    public void destroy() {
        fragmentMap.clear();
    }

    public ArticleListFragment getFragment(int position) {
        return fragmentMap.get(position);
    }

}
