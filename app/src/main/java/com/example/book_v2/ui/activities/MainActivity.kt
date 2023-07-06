package com.example.book_v2.ui.activities

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.book_v2.data.oop.*
import com.example.book_v2.services.interfaces.PageNavListeners
import com.example.book.ui.fragments.*
import com.example.book_v2.ui.predictImage.PredictImageFragment
import com.example.book_v2.R
import com.example.book_v2.data.oop.Book
import com.example.book_v2.data.oop.FactoryBuilder
import com.example.book_v2.data.oop.Page
import com.example.book_v2.data.oop.PageTypes
import com.example.book_v2.databinding.ActivityMainBinding
import com.example.book_v2.ui.fragments.ColoringPageFragment
import com.example.book_v2.ui.fragments.IntroductionPageFragment
import com.example.book_v2.ui.fragments.LargeLetterFragment
import com.example.book_v2.ui.fragments.MediumLetterFragment


class MainActivity : AppCompatActivity(), PageNavListeners {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bookPages: List<Page>
    private lateinit var book: Book
    private var currentPageNum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))


        bookPages = FactoryBuilder.BuildBookPages(intent?.extras?.getStringArrayList("DATA"))
        bookPages = bookPages.sortedBy { it.pageNum }

        setContentView(binding.root)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        for (i in bookPages) {
            Log.e("TAG", "onCreate: ${i.pageType}")
        }

//        val adapter = ViewPager2Adapter(supportFragmentManager, lifecycle)
//
//        for (i in bookPages.indices) {
//            adapter.addFragment(addFrags(i))
//        }
//
//
//        binding.viewPager.adapter = adapter
//
//        val bookFlipPageTransformer = BookFlipPageTransformer2()
//
//        bookFlipPageTransformer.isEnableScale = true
//
//        bookFlipPageTransformer.scaleAmountPercent = 10f
//
//        binding.viewPager.setPageTransformer(bookFlipPageTransformer)
//
//        binding.viewPager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
//        adapter.stateRestorationPolicy = FragmentStateAdapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
//        adapter.notifyDataSetChanged()
//        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            var lastPosition = 0
//            override fun onPageSelected(position: Int) {
//
//                super.onPageSelected(position)
//                if (position > lastPosition) {
//                    adapter.removeFragment(adapter.getFragment(0))
//                    Log.e("TAG", "onPageSelected: ${adapter.itemCount}")
//                }
//                lastPosition = position
//            }
//        })
//        adapter.stateRestorationPolicy = FragmentStateAdapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        // binding.viewPager.isUserInputEnabled = false

        book = Book()
        book.bookPages = bookPages

        pagesNavigation(currentPageNum)

    }

    private fun addFrags(pagePosition: Int): Fragment {

        val pageData = bookPages[pagePosition]
        val fragment: Fragment
        when (pageData.pageType) {
            PageTypes.INTRODUCTION_PAGE -> {
                fragment = IntroductionPageFragment(this, pageData as IntroductionPage)
            }
            PageTypes.LETTER_PAGE -> {
                fragment = LargeLetterFragment(this, pageData as LargeLetterPage)
            }
            PageTypes.MEDIUM_LETTER -> {
                fragment = MediumLetterFragment(this, pageData as MediumLetterPage)
            }
            PageTypes.SMALL_LETTER -> {
                fragment = SmallLetterFragment(this, pageData as SmallLetterPage)
            }
            PageTypes.FREE_MATCHING_PAGE -> {
                fragment = FreeMatchingFragment(this, pageData as FreeMatchingPage)
            }
            PageTypes.CONSTRAINED_MATCHING_PAGE -> {
                fragment = ConstrainedMatchingFragment(this, pageData as ConstrainedMatchingPage)
            }
            PageTypes.COLORING_PAGE -> {
                fragment = ColoringPageFragment(this, pageData as ColoringPage)
            }
            PageTypes.LETTER_FIRST_PREVIEW_PAGE -> {
                fragment = LetterFirstPreviewFragment(this, pageData as LetterFirstPreviewPage)
            }
            PageTypes.LETTER_SECOND_PREVIEW_PAGE -> {
                fragment = LetterSecondPreviewFragment(this, pageData as LetterSecondPreviewPage)
            }
            PageTypes.STORY_TELLING_PAGE -> {
                fragment = StoryTellingPageFragment(this, pageData as StoryTellingPage)
            }
            PageTypes.LETTER_PRONUNCIATION_PAGE -> {
                fragment =
                    LetterPronunciationPageFragment(this, pageData as LetterPronunciationPage)
            }
            else -> {
                throw Throwable("Error while transition to next page")
            }
        }
        return fragment
    }

    private fun pagesNavigation(pagePosition: Int) {

        val pageData = bookPages[pagePosition]
        val fragment: Fragment
        when (pageData.pageType) {
            PageTypes.INTRODUCTION_PAGE -> {
                fragment = IntroductionPageFragment(this, pageData as IntroductionPage)
                fragmentTransition(fragment)
            }
            PageTypes.LETTER_PAGE -> {
                fragment = LargeLetterFragment(this, pageData as LargeLetterPage)
                fragmentTransition(fragment)
            }
            PageTypes.MEDIUM_LETTER -> {
                fragment = MediumLetterFragment(this, pageData as MediumLetterPage)
                fragmentTransition(fragment)
            }
            PageTypes.SMALL_LETTER -> {
                fragment = SmallLetterFragment(this, pageData as SmallLetterPage)
                fragmentTransition(fragment)
            }
            PageTypes.FREE_MATCHING_PAGE -> {
                fragment = FreeMatchingFragment(this, pageData as FreeMatchingPage)
                fragmentTransition(fragment)
            }
            PageTypes.CONSTRAINED_MATCHING_PAGE -> {
                fragment = ConstrainedMatchingFragment(this, pageData as ConstrainedMatchingPage)
                fragmentTransition(fragment)
            }
            PageTypes.COLORING_PAGE -> {
                fragment = ColoringPageFragment(this, pageData as ColoringPage)

                fragmentTransition(fragment)
            }
            PageTypes.LETTER_FIRST_PREVIEW_PAGE -> {
                fragment = LetterFirstPreviewFragment(this, pageData as LetterFirstPreviewPage)
                fragmentTransition(fragment)
            }
            PageTypes.LETTER_SECOND_PREVIEW_PAGE -> {
                fragment = LetterSecondPreviewFragment(this, pageData as LetterSecondPreviewPage)
                fragmentTransition(fragment)
            }
            PageTypes.STORY_TELLING_PAGE -> {
                fragment = StoryTellingPageFragment(this, pageData as StoryTellingPage)
                fragmentTransition(fragment)
            }
            PageTypes.LETTER_PRONUNCIATION_PAGE -> {
                fragment =
                    LetterPronunciationPageFragment(this, pageData as LetterPronunciationPage)
                fragmentTransition(fragment)
            }
            PageTypes.LETTER_PREDICTION_PAGE -> {
                fragment =
                    PredictImageFragment(this, pageData as LetterPredictionPage)
                fragmentTransition(fragment)
            }
            else -> {
                throw Throwable("Error while transition to next page")
            }

        }
    }

    private fun fragmentTransition(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragments_container, fragment)
            .commit()
    }

    override fun nextPage() {
        if (currentPageNum < bookPages.size - 1) {
            currentPageNum++
            pagesNavigation(currentPageNum)
        }
    }

    override fun previousPage() {
        if (currentPageNum > 0) {
            currentPageNum--
            pagesNavigation(currentPageNum)
        }
    }
}
